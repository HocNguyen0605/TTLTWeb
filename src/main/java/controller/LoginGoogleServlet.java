package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.GooglePojo;
import util.GoogleUtils;

import java.io.IOException;

@WebServlet("/login-google")
public class LoginGoogleServlet extends HttpServlet {
    protected void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String code = request.getParameter("code");
        String returnState = request.getParameter("state");
        HttpSession session = request.getSession();
        String sessionState = (String) request.getSession().getAttribute("google_state");
        if (returnState == null || !returnState.equals(sessionState)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Yêu cầu giả mạo bị từ chối!");
            return;
        }

        if (code != null && !code.isEmpty()) {

            try {
                String accessToken = GoogleUtils.getToken(code);
                GooglePojo googlePojo = GoogleUtils.getUserInfo(accessToken);

                UserDAO dao = new UserDAO();
                model.User user = dao.processGoogleLogin(googlePojo);

                if (user != null) {
                    session.setAttribute("auth", user);
                    response.sendRedirect(request.getContextPath() + "/products");
                } else {
                    response.sendRedirect("login?error=auth_failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("login?error=auth_failed");
            }
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
