package controller.profile;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
import model.Order;
import dao.OrderDAO;

import java.io.IOException;
import java.util.List;

@WebServlet({"/profile", "/profile/*"})
public class ProfileController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("auth") == null) {
            response.sendRedirect("login");
            return;
        }

        User auth = (User) session.getAttribute("auth");
        OrderDAO orderDAO = new OrderDAO();
        List<Order> orders = orderDAO.getOrdersByUserId(auth.getId());
        request.setAttribute("userOrders", orders);

        String pathInfo = request.getPathInfo();
        String activeTab = "info";
        if (pathInfo != null && !pathInfo.isBlank()) {
            String normalized = pathInfo.trim().toLowerCase();
            if (normalized.startsWith("/")) normalized = normalized.substring(1);
            int slashIdx = normalized.indexOf('/');
            if (slashIdx >= 0) normalized = normalized.substring(0, slashIdx);

            if (normalized.equals("password")) activeTab = "password";
            else if (normalized.equals("orders")) activeTab = "orders";
            else if (normalized.equals("info") || normalized.isEmpty()) activeTab = "info";
            else {
                // Unknown subpath -> canonical default
                response.sendRedirect(request.getContextPath() + "/profile/info");
                return;
            }
        }
        request.setAttribute("activeTab", activeTab);

        if (session.getAttribute("errors") != null) {
            request.setAttribute("errors", session.getAttribute("errors"));
            session.removeAttribute("errors");
        }
        if (session.getAttribute("message") != null) {
            request.setAttribute("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }
        if (session.getAttribute("error") != null) {
            request.setAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }

        request.getRequestDispatcher("/view/user/profile/profile.jsp").forward(request, response);
    }
}
