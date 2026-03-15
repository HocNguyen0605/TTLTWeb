// admin-chart.js

document.addEventListener('DOMContentLoaded', function () {
    const ctx = document.getElementById('revenueChart');
    if (!ctx) return;

    let myChart = null;

    // Filter Elements
    const filterSelect = document.getElementById('chartFilter');
    const customRangeDiv = document.getElementById('customDateRange');
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    const btnFilter = document.getElementById('btnFilter');

    // 1. Initialize Chart
    function initChart(labels, data) {
        if (myChart) {
            myChart.destroy();
        }

        myChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Doanh thu (VNĐ)',
                    data: data,
                    borderColor: '#28a745',
                    backgroundColor: 'rgba(40, 167, 69, 0.1)',
                    borderWidth: 2,
                    pointBackgroundColor: '#28a745',
                    pointRadius: 4,
                    tension: 0.3,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                let label = context.dataset.label || '';
                                if (label) label += ': ';
                                if (context.parsed.y !== null) {
                                    label += new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(context.parsed.y);
                                }
                                return label;
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function (value) {
                                return new Intl.NumberFormat('vi-VN', { notation: "compact", compactDisplay: "short" }).format(value);
                            }
                        }
                    }
                }
            }
        });
    }

    // 2. Fetch Data Generic Function
    async function fetchData(filterType, start = null, end = null) {
        try {
            let url = `${contextPath}/admin/api/chart-data?filter=${filterType}`;
            if (filterType === 'custom' && start && end) {
                url += `&start=${start}&end=${end}`;
            }

            const response = await fetch(url);
            const result = await response.json();

            if (result.success) {
                initChart(result.labels, result.data);
            } else {
                console.error("Chart API Error:", result.message);
            }
        } catch (error) {
            console.error("Fetch Error:", error);
        }
    }

    // 3. Event Listeners

    // Dropdown Change
    if (filterSelect) {
        filterSelect.addEventListener('change', function () {
            const val = this.value;

            if (val === 'custom') {
                customRangeDiv.style.setProperty('display', 'flex', 'important');
            } else {
                customRangeDiv.style.setProperty('display', 'none', 'important');
                fetchData(val);
            }
        });
    }

    // Custom Filter Button
    if (btnFilter) {
        btnFilter.addEventListener('click', function () {
            const start = startDateInput.value;
            const end = endDateInput.value;

            if (!start || !end) {
                alert("Vui lòng chọn ngày bắt đầu và kết thúc!");
                return;
            }
            fetchData('custom', start, end);
        });
    }

    // 4. Load Default Data (7 days)
    // Use existing window data if available (fast load), otherwise fetch
    if (window.revenueLabels && window.revenueData) {
        initChart(window.revenueLabels, window.revenueData);
    } else {
        fetchData('7days');
    }
});
