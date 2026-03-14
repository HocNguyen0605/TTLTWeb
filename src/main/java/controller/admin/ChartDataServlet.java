package controller.admin;

import com.google.gson.Gson;
import dao.AdminDAO;
import util.DBContext;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/api/chart-data")
public class ChartDataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String filterType = req.getParameter("filter");
        String startDate = req.getParameter("start");
        String endDate = req.getParameter("end");

        if (filterType == null)
            filterType = "7days";

        try (Connection conn = DBContext.getConnection()) {
            AdminDAO adminDAO = new AdminDAO(conn);
            Map<String, Double> revenueMap = adminDAO.getRevenueStats(filterType, startDate, endDate);

            // Convert to separate arrays for Chart.js
            List<String> labels = new ArrayList<>(revenueMap.keySet());
            List<Double> data = new ArrayList<>(revenueMap.values());

            Gson gson = new Gson();
            String jsonOutput = gson.toJson(new ChartResponse(true, "Success", labels, data));

            try (PrintWriter out = resp.getWriter()) {
                out.print(jsonOutput);
                out.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = resp.getWriter()) {
                out.print("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
            }
        }
    }

    // Helper DTO class
    private static class ChartResponse {
        boolean success;
        String message;
        List<String> labels;
        List<Double> data;

        public ChartResponse(boolean success, String message, List<String> labels, List<Double> data) {
            this.success = success;
            this.message = message;
            this.labels = labels;
            this.data = data;
        }
    }
}
