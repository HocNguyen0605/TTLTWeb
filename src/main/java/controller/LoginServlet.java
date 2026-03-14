package controller;

import dao.UserDAO;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Kiểm tra nếu đã có Session rồi thì không cần check Cookie nữa
        HttpSession session = request.getSession(true);
        if (session != null && session.getAttribute("auth") != null) {
            User u = (User) session.getAttribute("auth");
            if (u.getRole() == 1) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/products");
            }
            return;
        }

        // 2. Check "Remember Me" cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String cuser = null;
            String cpass = null;
            for (Cookie c : cookies) {
                if ("cuser".equals(c.getName())) cuser = c.getValue();
                if ("cpass".equals(c.getName())) cpass = c.getValue();
            }

            if (cuser != null && cpass != null) {
                UserDAO dao = new UserDAO();
                User u = dao.login(cuser, cpass);
                if (u != null) {
                    request.getSession().setAttribute("auth", u);
                    if (u.getRole() == 1) {
                        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/products");
                    }
                    return;
                }
            }
        }
        // 3. Nếu không có Cookie hoặc Cookie sai, mới hiện trang Login
        request.getRequestDispatcher("/view/user/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user = request.getParameter("email"); // Input name in JSP is loginEmail (used for both
        // email/username)
        String pass = request.getParameter("password");

        String remember = request.getParameter("remember"); // Checkbox name

        UserDAO dao = new UserDAO();
        User u = dao.login(user, pass);

        if (u != null) {
            HttpSession session = request.getSession();
            session.setAttribute("auth", u);

            // Handle Cookies
            Cookie uCookie = new Cookie("cuser", user);
            Cookie pCookie = new Cookie("cpass", pass);
            uCookie.setPath("/");
            pCookie.setPath("/");
            if (remember != null) {
                uCookie.setMaxAge(60 * 60 * 24 * 7); // 7 days
                pCookie.setMaxAge(60 * 60 * 24 * 7);
            } else {
                uCookie.setMaxAge(0);
                pCookie.setMaxAge(0);
            }
            response.addCookie(uCookie);
            response.addCookie(pCookie);
            if (u.getRole() == 1) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/products");
            }
            return;
        } else {
            request.setAttribute("mess", "Sai tài khoản hoặc mật khẩu!");
            request.setAttribute("loginEmail", user); // Keep input
            request.getRequestDispatcher("/view/user/login.jsp").forward(request, response);
        }
    }
}