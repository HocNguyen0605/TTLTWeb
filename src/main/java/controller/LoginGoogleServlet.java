package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.GooglePojo;
import model.User;
import util.GoogleUtils;

import java.io.IOException;

@WebServlet("login-google")
public class LoginGoogleServlet extends HttpServlet {
    protected void doGet(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {

        String code = request.getParameter("code");
        String returnState = request.getParameter("state");
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

                User user = dao.processGoogleLogin(googlePojo);

                if (user != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("auth", user);
                    response.sendRedirect("products");
                } else {
                    response.sendRedirect("login?error=db_error");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("login_error=auth_failed");
            }
        }
    }
}
