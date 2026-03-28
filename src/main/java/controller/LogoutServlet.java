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

        Cookie cUser = new Cookie("cuser", "");
        Cookie cPass = new Cookie("cpass", "");
        cUser.setMaxAge(0);
        cPass.setMaxAge(0);
        cUser.setPath("/");
        cPass.setPath("/");
        response.addCookie(cUser);
        response.addCookie(cPass);

        response.sendRedirect(request.getContextPath() + "/home");
    }
}
