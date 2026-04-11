import {initRevenueChart} from './chart.js';
import {togglePassword} from "./togglePassword.js";

// Hàm chính để khởi tạo tất cả chức năng
function initApp() {
    // Ẩn hiện mật khẩu
    togglePassword();

    // Khởi tạo biểu đồ
    initRevenueChart();
}

document.addEventListener('DOMContentLoaded', initApp);

