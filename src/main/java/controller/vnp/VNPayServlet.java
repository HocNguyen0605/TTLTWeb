package controller.vnp;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        //thông tin cân gửi
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command","pay");
        params.put("vnp_TmnCode",VNPayConfig.vnp_TmnCode);
        long testAmount = 10000; // 10,000 VND
        params.put("vnp_Amount", String.valueOf(testAmount * 100));
//        params.put("vnp_Amount",String.valueOf(Long.parseLong(request.getParameter("amount")) * 100));
        params.put("vnp_CurrCode","VND");
        params.put("vnp_TxnRef","ORDER" + System.currentTimeMillis());
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
    }
}