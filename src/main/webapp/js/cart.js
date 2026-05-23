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
//Tính tổng tiền sản phẩm được tích chọn
document.addEventListener("DOMContentLoaded", function (){
    const checkBoxs= document.querySelectorAll(".cart-item-checkbox");
    function fetchCart(){
        let listProductIdSelected= [];
        checkBoxs.forEach(cb=>{
            if(cb.checked){
                listProductIdSelected.push(cb.getAttribute("data-product-id"))
            }
        });
        const params = new URLSearchParams();
        params.append("listIdSelected", listProductIdSelected.join(","));
        fetch(window.contextPath + '/cart?' + params.toString(), {
            method: "GET",
            headers: {
                "X-Requested-With": "XMLHttpRequest"
            }
        })
            .then(respone=> respone.json())
            .then(data => {
                const set = (id, val) => {
                    const el = document.getElementById(id);
                    if (el) el.innerText = formatCurrency(val);
                };
                set("totalPrice", data.totalPrice);
                set("discountPromotion", data.discountPromotion);
                set("discountVoucher", data.discountVoucher);
                set("shippingFee", data.shippingFee);
                set("totalDiscount", data.totalDiscount);
                set("total", data.total);
            })
            .catch(error=>console.error("Lỗi tính toán giỏ hàng: ", error));
    }
    //Format số tiền trước khi inner vào HTML
    function formatCurrency(amount){
        return amount.toLocaleString('vi-VN')+'đ';
    }
    checkBoxs.forEach(cb=>{
        cb.addEventListener("change", fetchCart);
    })
})