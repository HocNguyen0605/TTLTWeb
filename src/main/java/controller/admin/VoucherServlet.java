package controller.admin;

import dao.ProductDAO;
import dao.VoucherDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import dao.ProductDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;
import util.DBContext;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "VoucherServlet", value = "/admin/voucher")
public class VoucherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Connection conn = DBContext.getConnection();
            VoucherDAO voucherDAO = new VoucherDAO(conn);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        request.getRequestDispatcher("/view/admin/admin-CTKM.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}