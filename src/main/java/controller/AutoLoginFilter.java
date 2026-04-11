package controller;

import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;

@WebFilter(urlPatterns = "/*") // Áp dụng cho tất cả các trang
public class AutoLoginFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession();

        // Nếu chưa login
        if (session.getAttribute("auth") == null) {
            Cookie[] cookies = request.getCookies();
            String token = null;
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if ("rememberToken".equals(c.getName())) token = c.getValue();
                }
            }

            // Nếu tìm thấy Token, thực hiện login ngầm
            if (token != null) {
                UserDAO dao = new UserDAO();
                User u = dao.getUserByToken(token);
                if (u != null) {
                    session.setAttribute("auth", u);
                }
            }
        }
        chain.doFilter(req, res);
    }
}