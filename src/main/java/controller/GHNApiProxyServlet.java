package controller;

import util.GHNUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/ghn/*")
public class GHNApiProxyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid endpoint\"}");
            return;
        }

        try {
            String jsonResult = "";
            switch (pathInfo) {
                case "/province":
                    jsonResult = GHNUtils.getProvinces();
                    break;
                case "/district":
                    String provinceIdStr = req.getParameter("province_id");
                    if (provinceIdStr != null && !provinceIdStr.isEmpty()) {
                        jsonResult = GHNUtils.getDistricts(Integer.parseInt(provinceIdStr));
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        jsonResult = "{\"error\":\"Missing province_id\"}";
                    }
                    break;
                case "/ward":
                    String districtIdStr = req.getParameter("district_id");
                    if (districtIdStr != null && !districtIdStr.isEmpty()) {
                        jsonResult = GHNUtils.getWards(Integer.parseInt(districtIdStr));
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        jsonResult = "{\"error\":\"Missing district_id\"}";
                    }
                    break;
                default:
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    jsonResult = "{\"error\":\"Endpoint not found\"}";
                    break;
            }
            if (jsonResult == null) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResult = "{\"error\":\"Error connecting to GHN API\"}";
            }
            out.print(jsonResult);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"Internal Server Error\"}");
        }
    }
}
