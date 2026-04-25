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
import java.util.List;

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
        try {
            if ("add".equals(action)) {
                // Nếu có truyền quantity thì lấy,
                // nếu không (từ trang list) thì mặc định là 1
                int quantity = (quantityRaw != null) ? Integer.parseInt(quantityRaw) : 1;

                Product product = productDAO.findById(productId);
                if (product != null) {
                    cart.addProduct(product, quantity);
                }
            } else if ("remove".equals(action)) {
                cart.deleteProduct(productId);

            } else if ("update".equals(action)) {
                int quantity = (quantityRaw != null) ? Integer.parseInt(quantityRaw) : 1;
                cart.update(productId, quantity);
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
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
        //Tính số tiền giảm cho combo
        if (cart == null) return;
        try(Connection conn = DBContext.getConnection()){
            PromotionDAO promoDAO = new PromotionDAO(conn);
            PromotionComboItemDAO pciDAO = new PromotionComboItemDAO(conn);
            for (CartItem item : cart.getAllItems()) {
                int promoId = item.getProduct().getPromotion();
                if (promoId <= 0) continue;
                try {
                    Promotion promo = promoDAO.getById(promoId);
                    int productCountInCombo = pciDAO.countProductNeed(promoId);
                    //Nếu sản phẩm có nhiều hơn 1 sp tham gia
                    if (productCountInCombo > 1) {
                        List<PromotionComboItem> requiredItems = pciDAO.getItemsByComboId(promoId);
                        boolean isComboSatisfied = true;
                        int maxCombo = Integer.MAX_VALUE;

                        // Kiểm tra xem giỏ hàng có đủ các món trong list requiredItems không
                        for (PromotionComboItem req : requiredItems) {
                            CartItem cartItem = cart.findItemByProductId(req.getProductId());
                            if (cartItem == null || cartItem.getQuantity() < req.getQuantity()) {
                                isComboSatisfied = false;
                                break;
                            }
                            // Tính số combo
                            int comboCount= cartItem.getQuantity() / req.getQuantity();
                            if (comboCount < maxCombo) maxCombo = maxCombo;
                        }

                        if (isComboSatisfied) {
                            //tính số tiền
                            if ("percent".equals(promo.getDiscount_type())) {
                                discountPromotion += (item.getProduct().getPrice()* maxCombo* promo.getDiscount_value() *0.01);
                            } else {
                                discountPromotion += (promo.getDiscount_value() * maxCombo);
                            }
                        }
                        //Nếu chỉ có 1 sản phẩm cho chương trình
                    } else if (productCountInCombo == 1) {
                        List<PromotionComboItem> reqCombo = pciDAO.getItemsByComboId(promoId);
                        if (!reqCombo.isEmpty()) {
                            PromotionComboItem req = reqCombo.get(0);
                            if (item.getQuantity() >= req.getQuantity()) {
                                int maxCombo = item.getQuantity() / req.getQuantity();
                                if ("percent".equals(promo.getDiscount_type())) {
                                    discountPromotion += (item.getProduct().getPrice()* maxCombo* promo.getDiscount_value() *0.01);
                                } else {
                                    discountPromotion += (promo.getDiscount_value() * maxCombo);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }catch (Exception e){}
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
