//Tạo biểu đồ doanh thu
export function initRevenueChart() {
    const ctx = document.getElementById('revenueChart');

    if (!ctx) {
        // Không phải trang Admin hoặc chưa có canvas
        // console.error("Không tìm thấy canvas revenueChart");
        return;
    }

    // Đảm bảo thư viện Chart.js đã được tải
    if (typeof Chart === 'undefined') {
        console.error("Thư viện Chart.js chưa được tải.");
        return;
    }

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['1','5','10','15','20','25','30'],
            datasets: [{
                label: 'Doanh thu (VNĐ)',
                data: [200, 350, 500, 420, 680, 900, 1200],
                borderWidth: 3
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}