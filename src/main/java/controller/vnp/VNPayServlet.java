    package controller.vnp;

    import dao.OrderDAO;
    import jakarta.servlet.*;
    import jakarta.servlet.http.*;
    import jakarta.servlet.annotation.*;
    import model.Order;
    import model.User;
    import util.DBContext;

    import java.io.IOException;
    import java.net.URLEncoder;
    import java.nio.charset.StandardCharsets;
    import java.sql.Connection;
    import java.text.SimpleDateFormat;
    import java.util.*;

    @WebServlet("/payment/vnpay")
    public class VNPayServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            doPost(request, response);
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
           //Thông tin người dùng
            HttpSession session = request.getSession(false);
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            try(Connection conn = DBContext.getConnection()){
                OrderDAO orderDAO = new OrderDAO(conn);
                Order order= orderDAO.getOrderById(orderId);
                User user = (User) request.getSession().getAttribute("auth");

            //Kiểm tra thông tin
            String errorMsg = null;
            if (orderId == 0) {
                errorMsg="Mã đơn hàng không hợp lệ!";
            }
            if (user == null) {
                errorMsg = "Thông tin người dùng thay đổi. Vui lòng đăng nhập và thử lại";
            } else if(user.getId()!=order.getUserId()) {
                errorMsg = "Bạn không có quyền thanh toán đơn hàng này!";
            }
            if (errorMsg != null) {
                session.setAttribute("errorPayment", errorMsg);
                response.sendRedirect(request.getContextPath() + "/view/user/cart.jsp");
                return;
            }
            long  amount = (long) (order.getTotalPrice() * 100);
            //thông tin cân gửi
            Map<String, String> params = new HashMap<>();
            params.put("vnp_Version", "2.1.0");
            params.put("vnp_Command","pay");
            params.put("vnp_TmnCode",VNPayConfig.vnp_TmnCode);
            params.put("vnp_Amount",String.valueOf(amount));
            params.put("vnp_CurrCode","VND");
            params.put("vnp_TxnRef", "ORDER" + orderId + "_" + System.currentTimeMillis());
            params.put("vnp_OrderInfo","Thanh toan don hang");
            params.put("vnp_OrderType","other");
            params.put("vnp_Locale","vn");
            params.put("vnp_ReturnUrl",VNPayConfig.vnp_ReturnUrl);
            params.put("vnp_IpAddr",request.getRemoteAddr());
            params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

            //Sắp xếp dữ liệu theo yêu cầu vnp
            List<String> fields = new ArrayList<>(params.keySet());
            Collections.sort(fields);

            //tạo url và secure gửi đi
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            for(String field : fields){
                String value = params.get(field);
                String encodedKey = URLEncoder.encode(field, StandardCharsets.UTF_8.toString());
                String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
                hashData.append(encodedKey).append("=").append(encodedValue).append("&");
                query.append(encodedKey).append("=").append(encodedValue).append("&");
            }
            String queryStr = query.substring(0, query.length() - 1);
            String hashStr = hashData.substring(0, hashData.length() - 1);
            HmacSHA512 hmacSHA512 = new HmacSHA512();
            String secure = hmacSHA512.encrypt(VNPayConfig.vnp_HashSecret,hashStr);
            String payUrl = VNPayConfig.vnp_PayUrl + "?" + queryStr + "&vnp_SecureHash=" + secure;
            response.sendRedirect(payUrl);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }