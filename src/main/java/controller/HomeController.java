package controller;

import dao.BannerDAO;
import dao.ProductDAO;
import model.Banner;
import model.Product;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/home", ""})
public class HomeController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO dao = new ProductDAO();
        List<Product> featured = dao.getTopBestSeller();
        BannerDAO bannerDAO = new BannerDAO();
        List<Banner> banners = bannerDAO.getActiveBanners();

        request.setAttribute("featuredList", featured);
        request.setAttribute("banners", banners);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}