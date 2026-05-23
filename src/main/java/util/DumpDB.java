package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class DumpDB {
    public static void main(String[] args) {
        System.out.println("=== DIAGNOSING DATABASE SCHEMA ===");
        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("\n--- Create Table reviews ---");
            try (ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE reviews")) {
                if (rs.next()) {
                    System.out.println(rs.getString(2));
                }
            }

            System.out.println("\n--- Create Table orders ---");
            try (ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE orders")) {
                if (rs.next()) {
                    System.out.println(rs.getString(2));
                }
            }

            System.out.println("\n--- Create Table orderitems ---");
            try (ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE orderitems")) {
                if (rs.next()) {
                    System.out.println(rs.getString(2));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
