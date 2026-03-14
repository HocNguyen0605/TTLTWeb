// NAVBAR TOGGLE LOGIC
export function initNavbarToggle() {
    var navbarToggler = document.querySelector('.navbar-toggler');
    var navCollapse = document.querySelector('#navbarNav');

    if (navbarToggler && navCollapse) {
        navbarToggler.onclick = function() {
            //chuyển đổi class show (ẩn/hiện)
            navCollapse.classList.toggle('show');
            //chuyển đổi class collapse (cho hiệu ứng)
            navCollapse.classList.toggle('collapse');
        };
    }
}

// TABS LOGIC
export function initTabs() {
    var tabContainers = document.querySelectorAll('[role="tablist"]');

    // Lặp qua tất cả các container chứa tab
    for (var i = 0; i < tabContainers.length; i++) {
        var container = tabContainers[i];

        container.onclick = function(event) {
            var button = event.target.closest('[role="tab"]');
            if (!button) {
                return;
            }

            event.preventDefault();

            // 1. Tắt tab và panel đang hoạt động
            var activeTabs = container.querySelectorAll('.nav-link.active');
            for (var j = 0; j < activeTabs.length; j++) {
                activeTabs[j].classList.remove('active');
            }

            var activePanels = document.querySelectorAll('.tab-pane.show.active');
            for (var k = 0; k < activePanels.length; k++) {
                activePanels[k].classList.remove('show', 'active');
            }

            // 2. Kích hoạt tab mới
            button.classList.add('active');

            // 3. Kích hoạt panel mới
            var targetId = button.getAttribute('data-bs-target');
            if (targetId) {
                var targetPanel = document.querySelector(targetId);
                if (targetPanel) {
                    targetPanel.classList.add('show', 'active');
                }
            }
        };
    }
}
//add product
function addToCart(productId) {
    // Tạo dữ liệu để gửi đi
    const params = new URLSearchParams();
    params.append('action', 'add');
    params.append('productId', productId);
    params.append('quantity', '1');

    // Gửi yêu cầu AJAX bằng Fetch API
    fetch(window.contextPath+'/cart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
        },
        body: params
    })
        .then(response => {
            if (response.ok) {
                // Cập nhật số lượng trên icon Giỏ hàng mà không load lại trang
                updateCartBadge();
            }
        })
        .catch(error => console.error('Error:', error));
}

function updateCartBadge() {
    // Bạn có thể fetch lại số lượng mới từ server hoặc đơn giản là cộng thêm 1 vào Badge hiện tại
    let badge = document.querySelector('.badge');
    if (badge) {
        let currentCount = parseInt(badge.innerText) || 0;
        badge.innerText = currentCount + 1;
    } else {
        // Nếu chưa có badge (giỏ trống)
        location.reload();
    }
}
document.addEventListener('click', function (e) {
    if (e.target && e.target.classList.contains('btn-add-to-cart')) {
        const productId = e.target.getAttribute('data-id');
        addToCart(productId);
    }
});