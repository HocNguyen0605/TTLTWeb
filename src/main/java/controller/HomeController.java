package controller;

import dao.BannerDAO;
import dao.ProductDAO;
import model.Banner;
import model.Product;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import util.DBContext;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(urlPatterns = {"/home", ""})
public class HomeController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO dao = new ProductDAO();
        List<Product> featured = dao.getTopBestSeller();
        try(Connection conn = DBContext.getConnection()){
            BannerDAO bannerDAO = new BannerDAO(conn);
            List<Banner> banners = bannerDAO.getActiveBanners();
            request.setAttribute("banners", banners);
        }catch(Exception e){
            e.printStackTrace();
        }
        request.setAttribute("featuredList", featured);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}