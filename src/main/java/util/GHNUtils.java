package util;

import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import com.google.gson.JsonObject;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GHNUtils {
    public static final String GHN_TOKEN = ConfigLoader.getProperty("ghn.token.api");
    public static final String SHOP_ID = ConfigLoader.getProperty("ghn.shop.id");
    public static final String API_CREATE_ORDER = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create";
    public static final String API_TRACKING = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/detail";
    public static final String API_PROVINCE = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province";
    public static final String API_DISTRICT = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district";
    public static final String API_WARD = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward";
    public static final String API_FEE = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";

    public static String getProvinces() {
        try {
            return Request.Get(API_PROVINCE)
                    .addHeader("Token", GHN_TOKEN)
                    .execute()
                    .returnContent()
                    .asString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDistricts(int provinceId) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("province_id", provinceId);
            return Request.Post(API_DISTRICT)
                    .addHeader("Token", GHN_TOKEN)
                    .bodyString(json.toString(), ContentType.APPLICATION_JSON)
                    .execute()
                    .returnContent()
                    .asString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getWards(int districtId) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("district_id", districtId);
            return Request.Post(API_WARD)
                    .addHeader("Token", GHN_TOKEN)
                    .bodyString(json.toString(), ContentType.APPLICATION_JSON)
                    .execute()
                    .returnContent()
                    .asString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

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

    public static JsonObject createOrder(String toName, String toPhone, String toAddress, int districtId, String wardCode, int weight, com.google.gson.JsonArray items) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("payment_type_id", 2); // 2=người nhận trả phí vc
            json.addProperty("note", "Đơn hàng từ Juicy");
            json.addProperty("required_note", "CHOXEMHANGKHONGTHU");
            
            // Dest
            json.addProperty("to_name", toName);
            json.addProperty("to_phone", toPhone);
            json.addProperty("to_address", toAddress);
            json.addProperty("to_ward_code", wardCode); 
            json.addProperty("to_district_id", districtId); 
            
            json.addProperty("weight", weight);
            json.addProperty("length", 15);
            json.addProperty("width", 15);
            json.addProperty("height", 15);
            json.addProperty("service_type_id", 2); // Chuẩn
            
            json.add("items", items);

            HttpResponse response = Request.Post(API_CREATE_ORDER)
                    .addHeader("Token", GHN_TOKEN)
                    .addHeader("ShopId", SHOP_ID)
                    .bodyString(json.toString(), ContentType.APPLICATION_JSON)
                    .execute()
                    .returnResponse();
            
            String responseStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            System.out.println("GHN Create Order Response: " + responseStr);
            
            if (response.getStatusLine().getStatusCode() >= 400) {
                System.err.println("GHN API Error: " + responseStr);
                return null;
            }

            return JsonParser.parseString(responseStr).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static JsonObject calculateFee(int toDistrictId, String toWardCode, int weight) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("service_type_id", 2);
            json.addProperty("to_district_id", toDistrictId);
            json.addProperty("to_ward_code", toWardCode);
            json.addProperty("weight", weight);
            json.addProperty("length", 15);
            json.addProperty("width", 15);
            json.addProperty("height", 15);

            String responseStr = Request.Post(API_FEE)
                    .addHeader("Token", GHN_TOKEN)
                    .addHeader("ShopId", SHOP_ID)
                    .bodyString(json.toString(), ContentType.APPLICATION_JSON)
                    .execute()
                    .returnContent()
                    .asString(StandardCharsets.UTF_8);

            return JsonParser.parseString(responseStr).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}