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
            if ("add".equals(action)) {
                // Nếu có truyền quantity thì lấy,
                // nếu không (từ trang list) thì mặc định là 1
                int quantity = (quantityRaw != null) ? Integer.parseInt(quantityRaw) : 1;
                int countProduct = cart.getTotalQuantityByID(productId);
                int stock = productDAO.getMaxQuantityById(productId);
                if (countProduct+quantity<=stock) {
                    Product product = productDAO.findById(productId);
                    if (product != null) {
                        cart.addProduct(product, quantity);
                    }
                }

            } else if ("remove".equals(action)) {
                cart.deleteProduct(productId);

            } else if ("update".equals(action)) {
                int quantity = (quantityRaw != null) ? Integer.parseInt(quantityRaw) : 1;
                cart.update(productId, quantity);
            }

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
        Cart cart = (Cart) session.getAttribute("cart");
        Voucher voucher = (Voucher) session.getAttribute("voucher");
        double totalPrice= 0;
        double discountVoucher =0;
        double discountPromotion = 0;
        double totalDiscount = 0;
        double total=0;
        double shippingFee=15000;
        //Tinh so tien san pham
        if (cart != null && cart.getAllItems() != null && !cart.getAllItems().isEmpty()) {
            for (CartItem p : cart.getAllItems()) {
                totalPrice += p.getTotalPrice();
            }

            Set<Integer> promoIds = cart.getPromotionIdsFromCart(cart.getAllItems());
            //Tính số tiền giảm cho combo
            if (cart == null) return;
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
                        CartItem item = cart.findItemByProductId(pci.getProductId());
                        if (item == null || item.getQuantity() < pci.getQuantity()) {
                            isComboSatisfied = false;
                            break;
                        }
                        countCombo = item.getQuantity() / pci.getQuantity();
                        maxCombo = Math.min(maxCombo, countCombo);
                    }
                    for (PromotionComboItem pci : pciList) {
                        CartItem item = cart.findItemByProductId(pci.getProductId());
                        priceProductCombo += item.getPrice() * maxCombo;
                    }
                    if (isComboSatisfied) {
                        if (promotion.getDiscount_type().equals("percent")) {
                            discountPromotion = priceProductCombo * promotion.getDiscount_value() * 0.01;
                        } else discountPromotion = (promotion.getDiscount_value() * maxCombo);
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
        }
        totalDiscount = discountVoucher + discountPromotion;
        total=totalPrice-totalDiscount+shippingFee;
        request.setAttribute("totalPrice",totalPrice);
        request.setAttribute("discountPromotion",discountPromotion);
        request.setAttribute("discountVoucher",discountVoucher);
        request.setAttribute("totalDiscount",totalDiscount);
        request.setAttribute("total",total);

        request.setAttribute("shippingFee",shippingFee);

        // chỉ hiển thị cart
        request.getRequestDispatcher("/view/user/cart.jsp")
                .forward(request, response);
    }
}
