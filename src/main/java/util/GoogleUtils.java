package util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.GooglePojo;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

public class GoogleUtils {
    public static String CLIENT_ID = ConfigLoader.getProperty("google.client.id");
    public static String CLIENT_SECRET = ConfigLoader.getProperty("google.client.secret");
    public static String REDIRECT_URI = ConfigLoader.getProperty("google.redirect.uri");
    public static String LINK_GET_TOKEN = "https://oauth2.googleapis.com/token";
    public static String LINK_GET_USER_INFO = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";

    public static String getToken(final String code) throws Exception {
        String response = Request.Post(LINK_GET_TOKEN)
                .bodyForm(Form.form().add("client_id", CLIENT_ID)
                        .add("client_secret", CLIENT_SECRET)
                        .add("redirect_uri", REDIRECT_URI)
                        .add("code", code)
                        .add("grant_type", "authorization_code").build())
                .execute().returnContent().asString();
            JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        return jobj.get("access_token").toString().replaceAll("\"", "");
    }

    public static GooglePojo getUserInfo(final String accessToken) throws Exception {
        String link = LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        return new Gson().fromJson(response, GooglePojo.class);
    }
}
