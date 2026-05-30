package controller;

import dao.ProductDAO;
import dao.PromotionComboItemDAO;
import dao.PromotionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;
import util.DBContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@WebServlet("/cart")
public class CartController extends HttpServlet {

    private ProductDAO productDAO;
    @Override
    public void init() {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }
        String action = request.getParameter("action");
        int productId = Integer.parseInt(request.getParameter("productId"));
        String quantityRaw = request.getParameter("quantity");
        try(Connection conn = DBContext.getConnection()) {
            ProductDAO productDAO = new ProductDAO(conn);
            int stock = productDAO.getMaxQuantityById(productId);
            if ("add".equals(action)) {
                // Nếu có truyền quantity thì lấy,
                // nếu không (từ trang list) thì mặc định là 1
                int quantity = (quantityRaw != null) ? Integer.parseInt(quantityRaw) : 1;
                int countProduct = cart.getTotalQuantityByID(productId);
                if (countProduct+quantity<=stock) {
                    Product product = productDAO.findById(productId);
                    if (product != null) {
                        cart.addProduct(product, quantity);
                        if (session.getAttribute("auth") != null) {
                            User auth = (User) session.getAttribute("auth");
                            new dao.CartDAO(conn).addOrUpdateCartItem(auth.getId(), product.getId(), cart.findItemByProductId(product.getId()).getQuantity());
                        }
                    }
                } else session.setAttribute("messageCart","Chúng tôi hiện không đủ tồn! Rất xin lỗi quý khách");

            } else if ("remove".equals(action)) {
                cart.deleteProduct(productId);

                if (session.getAttribute("auth") != null) {
                    User auth = (User) session.getAttribute("auth");
                    new dao.CartDAO(conn).removeCartItem(auth.getId(), productId);
                }
            } else if ("update".equals(action)) {
                int quantity = (quantityRaw != null) ? Integer.parseInt(quantityRaw) : 1;
                if (quantity <= stock) {
                    cart.update(productId, quantity);
                } else {
                    session.setAttribute("messageCart", "Chúng tôi hiện không đủ hàng tồn! Rất xin lỗi quý khách");
                    cart.update(productId, stock);
                }
                if (session.getAttribute("auth") != null && cart.findItemByProductId(productId) != null) {
                    User auth = (User) session.getAttribute("auth");
                    new dao.CartDAO(conn).addOrUpdateCartItem(auth.getId(), productId, cart.findItemByProductId(productId).getQuantity());
                }            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        session.setAttribute("cart", cart);
// Kiểm tra nếu là yêu cầu AJAX thì chỉ trả về mã thành công
        String xRequestedWith = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(xRequestedWith) || request.getHeader("accept").contains("application/json")) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            // Nếu vẫn có những form cũ chưa sửa thì dùng redirect như cũ
            response.sendRedirect(request.getHeader("referer"));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        //Xử lý thông báo về giỏ hàng > tồn
        if (session != null) {
            String message = (String) session.getAttribute("messageCart");
            if (message != null) {
                request.setAttribute("messageCart", message);
                session.removeAttribute("messageCart");
            }
        }
        Cart cart = (Cart) session.getAttribute("cart");
        Voucher voucher = (Voucher) session.getAttribute("voucher");
        List<CartItem> listCalculate= new ArrayList<>();
        String listIdSelectedParam= request.getParameter("listIdSelected");
        double totalPrice= 0;
        double discountVoucher =0;
        double discountPromotion = 0;
        double totalDiscount = 0;
        double total=0;
        //Tinh so tien san pham
        if (cart != null && cart.getAllItems() != null && !cart.getAllItems().isEmpty()) {
            if(listIdSelectedParam!=null){
                List<String> listIds = Arrays.asList(listIdSelectedParam.split(","));
                for(CartItem item : cart.getAllItems()) {
                    if(listIds.contains(String.valueOf(item.getProduct().getId()))) {
                        listCalculate.add(item);
                        item.setChecked(true);
                    }
                    else item.setChecked(false);
                }
            }

            if(!listCalculate.isEmpty()){
                for(CartItem item : listCalculate) {
                    totalPrice+=item.getTotalPrice();
                }
            }

            Set<Integer> promoIds = cart.getPromotionIdsFromCart(listCalculate);
            //Tính số tiền giảm cho combo
            try (Connection conn = DBContext.getConnection()) {
                PromotionDAO promoDAO = new PromotionDAO(conn);
                PromotionComboItemDAO pciDAO = new PromotionComboItemDAO(conn);
                for (int id : promoIds) {
                    double priceProductCombo = 0.0;
                    if (id <= 0) continue;
                    Promotion promotion = promoDAO.getById(id);
                    List<PromotionComboItem> pciList = pciDAO.getItemsByComboId(id);
                    boolean isComboSatisfied = true;
                    int countCombo = 0;
                    int maxCombo = Integer.MAX_VALUE;
                    for (PromotionComboItem pci : pciList) {
                        CartItem item = null;
                        for(CartItem cartItem : listCalculate) {
                            if(cartItem.getProduct().getId() == pci.getProductId()) {
                                item = cartItem;
                                break;
                            }
                        }
                        if (item == null || item.getQuantity() < pci.getQuantity()) {
                            isComboSatisfied = false;
                            maxCombo=0;
                            break;
                        }
                        countCombo = item.getQuantity() / pci.getQuantity();
                        maxCombo = Math.min(maxCombo, countCombo);
                    }

                    if (isComboSatisfied && maxCombo > 0) {
                        for (PromotionComboItem pci : pciList) {
                            CartItem item = null;
                            for(CartItem cartItem : listCalculate) {
                                if(cartItem.getProduct().getId() == pci.getProductId()) {
                                    item = cartItem;
                                    priceProductCombo += item.getPrice() * (pci.getQuantity() * maxCombo);                                    break;
                                }
                            }
                        }
                        if (promotion.getDiscount_type().equals("percent")) {
                            discountPromotion += priceProductCombo * promotion.getDiscount_value() * 0.01;
                        } else discountPromotion += (promotion.getDiscount_value() * maxCombo);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Tính giảm giá voucher
        if (voucher != null) {
            if (voucher.getDiscountType().equals("percent")) {
                discountVoucher= voucher.getDiscountValue()/100 * totalPrice;
            } else if (voucher.getDiscountType().equals("amount")) {
                discountVoucher= voucher.getDiscountValue();
            }
            session.removeAttribute("voucher");
        }
        double shippingFee = listCalculate.isEmpty() ? 0 : 15000;
        totalDiscount = discountVoucher + discountPromotion;
        total = (totalPrice - totalDiscount + shippingFee)>0? totalPrice - totalDiscount + shippingFee:0 ;
        //Hàm response cho AJAX của JS
        String requestedWith = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestedWith)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String json = String.format(
                    "{\"totalPrice\":%.2f,\"discountPromotion\":%.2f,\"discountVoucher\":%.2f,\"totalDiscount\":%.2f,\"shippingFee\":%.2f,\"total\":%.2f}",
                    totalPrice, discountPromotion, discountVoucher, totalDiscount, shippingFee, total
            );
            response.getWriter().write(json);
        }else {

            request.setAttribute("totalPrice", totalPrice);
            request.setAttribute("discountPromotion", discountPromotion);
            request.setAttribute("discountVoucher", discountVoucher);
            request.setAttribute("totalDiscount", totalDiscount);
            request.setAttribute("total", total);

            request.setAttribute("shippingFee", shippingFee);

            // chỉ hiển thị cart
            request.getRequestDispatcher("/view/user/cart.jsp")
                    .forward(request, response);
        }
    }
}
