package controller.admin;

import dao.BannerDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import model.Banner;

import java.io.PrintWriter;
import java.util.List;

@WebServlet("/search-banner-ajax")
public class SearchBannerAJAX extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String keyword = request.getParameter("keyword");
        BannerDAO dao = new BannerDAO();
        List<Banner> list = dao.getBannerByTitle(keyword);

        PrintWriter out = response.getWriter();
        for (Banner b : list) {
            out.println("<a href='banner?search=" + b.getTitle() + "' class='list-group-item list-group-item-action d-flex align-items-center'>");
            out.println("<img src='" + b.getImageUrl() + "' style='width:40px; height:40px; object-fit:cover;' class='me-3'>");
            out.println("<div>");
            out.println("<div class='fw-bold'>" + b.getTitle() + "</div>");
            out.println("<small class='text-muted'>Trạng thái: " + (b.getIsActive() ? "Active" : "Ẩn") + "</small>");
            out.println("</div>");
            out.println("</a>");
        }
    }
}