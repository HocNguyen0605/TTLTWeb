package dao;

import java.io.IOException;
import java.util.Properties;

public class DBproperties {
    private static final Properties prop = new Properties();

    static {
        try {
            prop.load(DBproperties.class.getClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static String getDbHost() {
        return prop.get("db.host").toString();
    }

    public static String getDbPort() {
        return prop.get("db.port").toString();
    }

    public static String getUsername() {
        return prop.get("db.username").toString();
    }

    public static String getPassword() {
        Object pass = prop.get("db.password");
        return (pass == null) ? "" : pass.toString();
    }

    public static String getDbOption() {
        return prop.get("db.dbOption").toString();
    }

    public static String getDbName(){
        return prop.getProperty("db.dbName"); // Dùng getProperty sẽ chuẩn hơn cho file .properties
    }
}
