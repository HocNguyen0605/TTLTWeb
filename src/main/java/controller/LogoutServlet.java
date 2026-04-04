package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Cookie token = new Cookie("rememberToken", "");
        token.setMaxAge(0);
        token.setHttpOnly(true);
        token.setPath("/");
        response.addCookie(token);

        response.sendRedirect(request.getContextPath() + "/home");
    }
}
