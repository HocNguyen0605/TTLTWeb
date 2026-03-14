package controller.admin;

import dao.ProductDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "HiddenProductServlet", value = "/HiddenProductServlet")
public class HiddenProductServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                String action = request.getParameter("action");
                int id = Integer.parseInt(request.getParameter("id"));

//                if ("delete".equals(action)) {
//                    ProductDAO dao = new ProductDAO();
//                    dao.hide(id); // hoặc update status = 'inactive'
//                }
//
//                response.sendRedirect("admin-product.jsp?success=hide");
            }
        }