package util;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import com.google.gson.JsonObject;
import java.io.IOException;

public class GHNUtils {
    public static final String GHN_TOKEN = ConfigLoader.getProperty("ghn.token.api");
    public static final String SHOP_ID = ConfigLoader.getProperty("ghn.shop.id");
    public static final String API_CREATE_ORDER = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create";
    public static final String API_TRACKING = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/detail";

    public static String getOrderTracking(String shippingCode) {
        if (shippingCode == null || shippingCode.trim().isEmpty()) {
            return null;
        }
        try {
            JsonObject json = new JsonObject();
            json.addProperty("order_code", shippingCode);

            return Request.Post(API_TRACKING)
                    .addHeader("Token", GHN_TOKEN)
                    .bodyString(json.toString(), ContentType.APPLICATION_JSON)
                    .execute()
                    .returnContent()
                    .asString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JsonObject createOrder(String toName, String toPhone, String toAddress, com.google.gson.JsonArray items) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("payment_type_id", 1); // Shop trả phí
            json.addProperty("note", "Đơn hàng từ Juicy");
            json.addProperty("required_note", "CHOXEMHANGKHONGTHU");
            
            // Dest
            json.addProperty("to_name", toName);
            json.addProperty("to_phone", toPhone);
            json.addProperty("to_address", toAddress);
            // Dùng mã quận/huyện mặc định để test do frontend chưa có dropdown chọn địa chỉ chuẩn GHN
            json.addProperty("to_ward_code", "20308"); 
            json.addProperty("to_district_id", 1442); 
            
            json.addProperty("weight", 200);
            json.addProperty("length", 15);
            json.addProperty("width", 15);
            json.addProperty("height", 15);
            json.addProperty("service_type_id", 2); // Chuẩn
            
            json.add("items", items);

            String responseStr = Request.Post(API_CREATE_ORDER)
                    .addHeader("Token", GHN_TOKEN)
                    .addHeader("ShopId", SHOP_ID)
                    .bodyString(json.toString(), ContentType.APPLICATION_JSON)
                    .execute()
                    .returnContent()
                    .asString();

            return com.google.gson.JsonParser.parseString(responseStr).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}