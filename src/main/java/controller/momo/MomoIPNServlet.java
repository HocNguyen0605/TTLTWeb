package controller.momo;

import dao.OrderDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import util.DBContext;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/momo-ipn")
public class MomoIPNServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String resultCode = request.getParameter("resultCode");
        int orderId = Integer.parseInt(request.getParameter("orderId"));

        try (Connection conn = DBContext.getConnection()) {
            OrderDAO orderDAO = new OrderDAO(conn);

            if ("0".equals(resultCode)) {
                orderDAO.updateStatus(orderId, "PAID");
            } else {
                orderDAO.updateStatus(orderId, "FAILED");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("OK");
    }
}
