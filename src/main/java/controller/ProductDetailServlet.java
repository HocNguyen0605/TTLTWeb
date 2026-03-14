//package controller;
//
//import service.ProductService;
//import model.Product;
//import jakarta.servlet.*;
//import jakarta.servlet.http.*;
//import jakarta.servlet.annotation.*;
//import java.io.IOException;
//
//@WebServlet(name = "ProductDetailServlet", value = "/product-detail")
//public class ProductDetailServlet extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // 1. Ép mã UTF-8 để hiển thị tiếng Việt không bị lỗi
//        request.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html;charset=UTF-8");
//
//        // 2. Lấy ID sản phẩm từ URL
//        String idParam = request.getParameter("id");
//
//        if (idParam != null) {
//            try {
//                int id = Integer.parseInt(idParam);
//                ProductService service = new ProductService();
//
//                // 3. Gọi hàm lấy chi tiết sản phẩm (đã xử lý logic 1 hình ảnh)
//                Product p = service.getProductDetail(id);
//
//                if (p != null) {
//                    request.setAttribute("product", p);
//                    // 4. Chuyển hướng sang trang chi tiết (nằm cùng thư mục với products.jsp)
//                    request.getRequestDispatcher("/WEB-INF/user/product-detail.jsp").forward(request, response);
//                } else {
//                    response.sendRedirect("products"); // Không thấy sản phẩm thì về danh sách
//                }
//            } catch (NumberFormatException e) {
//                response.sendRedirect("products");
//            }
//        } else {
//            response.sendRedirect("products");
//        }
//    }
//}