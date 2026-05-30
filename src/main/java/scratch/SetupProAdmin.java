package scratch;

import util.DBContext;
import org.jdbi.v3.core.Jdbi;

public class SetupProAdmin {
    public static void main(String[] args) {
        Jdbi jdbi = DBContext.getJdbi();
        jdbi.useHandle(handle -> {
            try {
                //thay đổi kiểu dữ liệu thành VARCHAR
                handle.execute("ALTER TABLE account MODIFY COLUMN role VARCHAR(20) DEFAULT 'user'");
                System.out.println("Modified role column to VARCHAR");
            } catch (Exception e) {
                System.out.println("Error modifying column: " + e.getMessage());
            }

            try {
                //Tạo pro-admin cho user id = 1
                int updated = handle.createUpdate("UPDATE account SET role = 'pro-admin' WHERE id = 1")
                        .execute();
                System.out.println("Updated user 1 to pro-admin: " + updated + " rows affected.");
            } catch (Exception e) {
                System.out.println("Error updating user: " + e.getMessage());
            }
        });
    }
}
