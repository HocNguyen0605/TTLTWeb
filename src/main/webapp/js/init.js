import { initCarousel } from './carousel.js';
import { initRevenueChart } from './chart.js';
import { numberFormat} from  './number-format.js';


// Hàm chính để khởi tạo tất cả chức năng
function initApp() {
    // Chỉ khởi tạo carousel
    if (document.getElementById('hero-carousel')) {
        // Tên id và interval (ms)
        initCarousel('hero-carousel', 3000);
    }

    // Khởi tạo biểu đồ
    initRevenueChart();

    // format giá tiền
    numberFormat();
}

document.addEventListener('DOMContentLoaded', initApp);

