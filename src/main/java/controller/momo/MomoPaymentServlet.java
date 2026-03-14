package controller.momo;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import model.HmacSHA256;
import util.HttpUtil;
@WebServlet("/momo-payment")
public class MomoPaymentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int orderId = Integer.parseInt(request.getParameter("orderId"));

        // === CONFIG MOMO ===
        String endpoint = "https://test-payment.momo.vn/v2/gateway/api/create";
        String partnerCode = "MOMO";
        String accessKey = "YOUR_ACCESS_KEY";
        String secretKey = "YOUR_SECRET_KEY";

        String amount = request.getParameter("amount"); // hoặc query DB
        String orderInfo = "Thanh toán đơn hàng #" + orderId;
        String redirectUrl = "http://localhost:8080/yourapp/momo-return";
        String ipnUrl = "http://localhost:8080/yourapp/momo-ipn";

        String requestId = String.valueOf(System.currentTimeMillis());
        String orderIdStr = String.valueOf(orderId);
        String requestType = "captureWallet";

        // === SIGNATURE STRING ===
        String rawHash =
                "accessKey=" + accessKey +
                        "&amount=" + amount +
                        "&extraData=" +
                        "&ipnUrl=" + ipnUrl +
                        "&orderId=" + orderIdStr +
                        "&orderInfo=" + orderInfo +
                        "&partnerCode=" + partnerCode +
                        "&redirectUrl=" + redirectUrl +
                        "&requestId=" + requestId +
                        "&requestType=" + requestType;

        String signature = HmacSHA256.sign(rawHash, secretKey);

        // === JSON BODY ===
        String json = """
        {
          "partnerCode":"%s",
          "accessKey":"%s",
          "requestId":"%s",
          "amount":"%s",
          "orderId":"%s",
          "orderInfo":"%s",
          "redirectUrl":"%s",
          "ipnUrl":"%s",
          "requestType":"%s",
          "signature":"%s",
          "lang":"vi"
        }
        """.formatted(
                partnerCode, accessKey, requestId, amount,
                orderIdStr, orderInfo, redirectUrl, ipnUrl,
                requestType, signature
        );

        // === GỬI HTTP POST ===
        String responseJson = HttpUtil.post(endpoint, json);

        JsonObject obj = JsonParser.parseString(responseJson).getAsJsonObject();
        String payUrl = obj.get("payUrl").getAsString();

        response.sendRedirect(payUrl);

    }
}
