import {initRevenueChart} from './chart.js';
import {togglePassword} from "./togglePassword.js";
import {initChangePassword} from "./change-password.js";
import { initLogoutConfirmation } from "./logout.js";

// Hàm chính để khởi tạo tất cả chức năng
function initApp() {
    // Ẩn hiện mật khẩu
    togglePassword();

    // Khởi tạo biểu đồ
    initRevenueChart();

    // Logic xóa field đổi mk
    initChangePassword();

    // Thông báo xác nhận logout
    const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 1));
    initLogoutConfirmation(contextPath);
}

document.addEventListener('DOMContentLoaded', initApp);

