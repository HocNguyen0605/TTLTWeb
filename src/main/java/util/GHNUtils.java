package util;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import com.google.gson.JsonObject;
import java.io.IOException;

public class GHNUtils {
    public static final String GHN_TOKEN = ConfigLoader.getProperty("ghn.token.api");
    public static final String SHOP_ID = ConfigLoader.getProperty("ghn.shop.id");
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
}