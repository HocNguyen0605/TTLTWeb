package controller.momo;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
@WebServlet("/momo-return")
public class MomoReturnServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String resultCode = req.getParameter("resultCode");

        if ("0".equals(resultCode)) {
            resp.sendRedirect("success.jsp");
        } else {
            resp.sendRedirect("fail.jsp");
        }
    }
}
