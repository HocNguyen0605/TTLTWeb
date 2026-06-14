package controller.vnp;

import dao.CartDAO;
import dao.OrderDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Cart;
import model.CartItem;
import model.HmacSHA256;
import model.User;
import util.DBContext;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.*;

@WebServlet(name = "VNPayReturnServlet ", value = "/vnpay_return")
public class VNPayReturnServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        //Kiểm tra tính nguyên vẹn của dữ liệu callback
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        Map<String,String> params = new HashMap<>();
        for(Map.Entry<String,String[]> entry: request.getParameterMap().entrySet()){
            params.put(entry.getKey(),entry.getValue()[0]);
        }
        params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");

        List<String> fields = new ArrayList<>(params.keySet());
        Collections.sort(fields);
        StringBuilder hashData  = new StringBuilder();
        for (String field : fields) {
            String value = params.get(field);
            hashData.append(URLEncoder.encode(field, StandardCharsets.UTF_8)).append("=")
                    .append(URLEncoder.encode(value,StandardCharsets.UTF_8)).append("&");
        }

        String hashStr = hashData.substring(0, hashData.length() - 1);
        HmacSHA512 hmacSHA512 = new HmacSHA512();
        String urlEncrypt = hmacSHA512.encrypt(VNPayConfig.vnp_HashSecret,hashStr);
        String errorReturnPm;
        if(!urlEncrypt.equals(vnp_SecureHash)){
            errorReturnPm="Thông tin gửi về đã bị chỉnh sửa";
            session.setAttribute("errorReturnPm",errorReturnPm);
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        User user = (User) session.getAttribute("auth");
        Cart cart = (Cart) session.getAttribute("cart");
        List<CartItem> cartItemsChecked = cart.getSelectedItemsForCheckout();

        //lấy dữ liệu đc return
        String txnRef = request.getParameter("vnp_TxnRef");
        int orderId = Integer.parseInt(txnRef.split("_")[0].replace("ORDER", ""));
        String responseCode = request.getParameter("vnp_ResponseCode");
        Connection conn = null;
        try{
            conn = DBContext.getConnection();
            CartDAO cartDAO = new CartDAO(conn);
            OrderDAO orderDAO = new OrderDAO(conn);
        if ("00".equals(responseCode)) {
                conn.setAutoCommit(false);
                // Xóa item ra khỏi cart session và db
                for (CartItem item : cartItemsChecked) {
                    if (user != null) {
                        cart.deleteProduct(item.getProduct().getId());
                        cartDAO.removeCartItem(user.getId(), item.getProduct().getId());
                    }
                }
            orderDAO.updateStatusPayment(orderId, "PAID");
            conn.commit();
            request.getRequestDispatcher("/view/user/cart.jsp").forward(request, response);

        } else {
            errorReturnPm="Thanh toán lỗi";
            orderDAO.updateStatusPayment(orderId, "Thanh toán lỗi");
            session.setAttribute("errorReturnPm",errorReturnPm);
            request.getRequestDispatcher("/view/user/cart.jsp").forward(request, response);
        }

        }catch (Exception e){
            e.printStackTrace();
            try { conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace(); }
        }
        }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}