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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = userDAO.getAllUsers();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/view/admin/admin-accounts.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");

        if (idStr != null && !idStr.isEmpty()) {
            int id = Integer.parseInt(idStr);

            if ("updateRole".equals(action)) {
                String newRole = req.getParameter("role");
                if (newRole != null && (newRole.equals("admin") || newRole.equals("user"))) {
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
