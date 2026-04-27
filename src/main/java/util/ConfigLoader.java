package util;

import java.util.Properties;
import java.io.InputStream;

public class ConfigLoader {
    private static Properties prop = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("google.properties")) {
            if (input == null) {
                System.out.println("Xin lỗi, không tìm thấy file google.properties");
            } else {
                prop.load(input);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
}
