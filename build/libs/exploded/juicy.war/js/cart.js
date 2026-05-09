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