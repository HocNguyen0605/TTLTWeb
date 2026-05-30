package controller.admin;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/accounts")
public class AccountController extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    public void init() throws ServletException {
        try {
            util.DBContext.getJdbi().useHandle(handle -> {
                // Đảm bảo cột role là VARCHAR để chứa được 'pro-admin'
                handle.execute("ALTER TABLE account MODIFY COLUMN role VARCHAR(20) DEFAULT 'user'");
                // Tự động thăng cấp tài khoản 'admin' lên pro-admin để không bị mất quyền
                handle.execute("UPDATE account SET role = 'pro-admin' WHERE username = 'admin'");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User authUser = (User) req.getSession().getAttribute("auth");
        // Chỉ Pro-Admin mới được truy cập
        if (authUser == null || authUser.getRole() != 2) {
            req.getRequestDispatcher("/view/user/403.jsp").forward(req, resp);
            return;
        }

        // Bước 2: Kiểm tra OTP
        if (!Boolean.TRUE.equals(req.getSession().getAttribute("pro_verified"))) {
            resp.sendRedirect(req.getContextPath() + "/admin/verify-pro");
            return;
        }

        List<User> users = userDAO.getAllUsers();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/view/admin/admin-accounts.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User authUser = (User) req.getSession().getAttribute("auth");
        // Chỉ Pro-Admin mới được thay đổi quyền hoặc xóa
        if (authUser == null || authUser.getRole() != 2) {
            req.getRequestDispatcher("/view/user/403.jsp").forward(req, resp);
            return;
        }

        // Bước 2: Kiểm tra OTP (đề phòng ai đó gọi API trực tiếp qua Postman mà chưa verify OTP)
        if (!Boolean.TRUE.equals(req.getSession().getAttribute("pro_verified"))) {
            resp.sendRedirect(req.getContextPath() + "/admin/verify-pro");
            return;
        }

        String action = req.getParameter("action");
        String idStr = req.getParameter("id");

        if (idStr != null && !idStr.isEmpty()) {
            int id = Integer.parseInt(idStr);

            if ("updateRole".equals(action)) {
                String newRole = req.getParameter("role");
                if (newRole != null && (newRole.equals("admin") || newRole.equals("user") || newRole.equals("pro-admin"))) {
                    boolean success = userDAO.updateUserRole(id, newRole);
                    if (success) {
                        req.getSession().setAttribute("message", "Cập nhật quyền thành công!");
                    } else {
                        req.getSession().setAttribute("error", "Lỗi khi cập nhật quyền thành công.");
                    }
                }
            } else if ("delete".equals(action)) {
                boolean success = userDAO.deleteAccount(id);
                if (success) {
                    req.getSession().setAttribute("message", "Xoá tài khoản thành công!");
                } else {
                    req.getSession().setAttribute("error", "Không thể xoá tài khoản này do đã có ràng buộc dữ liệu (đã có đơn hàng) hoặc lỗi hệ thống.");
                }
            }
        }

        resp.sendRedirect(req.getContextPath() + "/admin/accounts");
    }
}
