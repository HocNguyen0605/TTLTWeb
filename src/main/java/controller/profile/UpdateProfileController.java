package controller.profile;

import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
import java.io.IOException;

@WebServlet("/updateProfile")
public class UpdateProfileController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("auth");

        boolean isApi = request.getHeader("Accept") != null && request.getHeader("Accept").contains("application/json");

        if (user == null) {
            if (isApi) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                java.util.Map<String, String> result = new java.util.HashMap<>();
                result.put("status", "error");
                result.put("message", "Vui lòng đăng nhập lại.");
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
            } else {
                response.sendRedirect("login");
            }
            return;
        }

        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // Validate
        validator.ProfileValidator validator = new validator.ProfileValidator();
        java.util.Map<String, String> errors = validator.validate(fullName, phone);

        if (!errors.isEmpty()) {
            if (isApi) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                java.util.Map<String, Object> result = new java.util.HashMap<>();
                result.put("status", "error");
                result.put("errors", errors);
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
            } else {
                session.setAttribute("errors", errors);
                response.sendRedirect(request.getContextPath() + "/profile");
            }
            return;
        }

        UserDAO dao = new UserDAO();

        user.setUsername(request.getParameter("username"));
        user.setFullName(fullName);
        user.setPhone(phone != null && phone.trim().isEmpty() ? null : phone.trim());

        user.setAddress(address != null && address.trim().isEmpty() ? null : address.trim());
        
        String provinceIdStr = request.getParameter("provinceId");
        String districtIdStr = request.getParameter("districtId");
        String wardCode = request.getParameter("wardCode");
        
        if (provinceIdStr != null && !provinceIdStr.isEmpty()) {
            user.setProvinceId(Integer.parseInt(provinceIdStr));
        } else {
            user.setProvinceId(null);
        }
        
        if (districtIdStr != null && !districtIdStr.isEmpty()) {
            user.setDistrictId(Integer.parseInt(districtIdStr));
        } else {
            user.setDistrictId(null);
        }
        
        if (wardCode != null && !wardCode.isEmpty()) {
            user.setWardCode(wardCode);
        } else {
            user.setWardCode(null);
        }

        session.removeAttribute("errors");

        if (dao.updateProfile(user)) {
            session.setAttribute("auth", user);
            if (isApi) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                java.util.Map<String, String> result = new java.util.HashMap<>();
                result.put("status", "success");
                result.put("message", "Cập nhật thông tin thành công!");
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
            } else {
                session.setAttribute("message", "Cập nhật thông tin thành công!");
                response.sendRedirect(request.getContextPath() + "/profile");
            }
        } else {
            if (isApi) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                java.util.Map<String, String> result = new java.util.HashMap<>();
                result.put("status", "error");
                result.put("message", "Cập nhật thất bại!");
                response.getWriter().write(new com.google.gson.Gson().toJson(result));
            } else {
                session.setAttribute("error", "Cập nhật thất bại!");
                response.sendRedirect(request.getContextPath() + "/profile");
            }
        }
    }
}
