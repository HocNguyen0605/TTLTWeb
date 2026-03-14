package controller.admin;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        // 1. Lấy đối tượng User từ session thông qua key "auth"
        model.User user = (session != null) ? (model.User) session.getAttribute("auth") : null;

        // 2. Kiểm tra: Nếu chưa đăng nhập HOẶC không phải là Admin (role != 1)
        // 2. Kiểm tra:
        if (user == null) {
            // Chưa đăng nhập -> Redirect về login
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        } else if (user.getRole() != 1) {
            // Đã đăng nhập nhưng không phải admin -> Forward sang 403
            request.getRequestDispatcher("/view/user/403.jsp").forward(request, response);
            return;
        }

        // 3. Nếu mọi thứ ổn, cho phép đi tiếp
        chain.doFilter(req, res);
    }
}
