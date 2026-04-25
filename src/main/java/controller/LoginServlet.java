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

        HttpSession session = request.getSession(true);
       //dành cho đánh giá sản phẩm
        String returnUrl = request.getParameter("returnUrl");
        if (returnUrl != null) {
            session.setAttribute("returnUrl", returnUrl);
        }
        //
        if (session != null && session.getAttribute("auth") != null) {
            User u = (User) session.getAttribute("auth");
            if (u.getRole() == 1) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            //dánh giá
            } else {
                String target = (String) session.getAttribute("returnUrl");
                if (target != null) {
                    session.removeAttribute("returnUrl");
                    response.sendRedirect(target);
                    //
                } else {
                    response.sendRedirect(request.getContextPath() + "/products");
                }
            }
            return;
        }

        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("rememberToken".equals(c.getName())) token = c.getValue();
            }
        }
        if (token != null) {
            UserDAO dao = new UserDAO();
            User u = dao.getUserByToken(token);
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

        String[] flashAttributes = {"errors", "oldUsername", "oldFullName", "oldEmail", "activeTab", "error", "mess"};
        for (String attr : flashAttributes) {
            if (session.getAttribute(attr) != null) {
                request.setAttribute(attr, session.getAttribute(attr));
                session.removeAttribute(attr);
            }
        }
        request.getRequestDispatcher("/view/user/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user = request.getParameter("email");
        String pass = request.getParameter("password");
        String remember = request.getParameter("remember");
        UserDAO dao = new UserDAO();
        User u = dao.login(user, pass);

        if (u != null) {
            HttpSession session = request.getSession();
            session.setAttribute("auth", u);

            // Xử lí Cookies
            if (remember != null) {
                String randToken = java.util.UUID.randomUUID().toString();

                dao.saveUserToken(u.getId(), randToken);

                Cookie tokenCookie = new Cookie("rememberToken", randToken);
                tokenCookie.setMaxAge(60 * 60 * 24 * 7);
                tokenCookie.setHttpOnly(true);
                tokenCookie.setPath("/");
                response.addCookie(tokenCookie);
            }

            if (u.getRole() == 1) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            //đánh giá
            } else {
                String returnUrl = (String) session.getAttribute("returnUrl");
                if (returnUrl != null) {
                    session.removeAttribute("returnUrl");
                    response.sendRedirect(returnUrl);
                    //
                } else {
                    response.sendRedirect(request.getContextPath() + "/products");
                }
            }
            return;
        } else {
            request.setAttribute("mess", "Sai tài khoản hoặc mật khẩu!");
            request.setAttribute("loginEmail", user);
            request.getRequestDispatcher("/view/user/login.jsp").forward(request, response);
        }
    }
}