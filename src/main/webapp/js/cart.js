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
function fetchCart(){
        const checkBoxs = document.querySelectorAll(".cart-item-checkbox");
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
        .catch(error=>console.error ("Lỗi tính toán giỏ hàng: ", error));
}
function formatCurrency(amount){
    return amount.toLocaleString('vi-VN')+'đ';
}
//áp dụng voucher bằng AJAX
function applyVoucher(){
    const code = document.getElementById("codeVoucher").value.trim();
    const params = new URLSearchParams();
    params.append("codeVoucher",code);
    fetch(window.contextPath+ '/apply-voucher',{
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            "X-Requested-With": "XMLHttpRequest"
        },
        body: params.toString()
    }).then(respone=>respone.json())
        .then(data=>{
            const message= document.getElementById("voucherMessage");
            if(data.success){
                message.innerHTML='<span class="text-success">Áp dụng voucher thành công</span>';
            }else {
                message.innerHTML = `<span class="text-danger">${data.error}</span>`;
            }
            fetchCart();
        }).catch(error=> console.error("Lỗi voucher: ", error));
}
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".cart-item-checkbox").forEach(cb => {
        cb.addEventListener("change", fetchCart);
    });
    const btn = document.getElementById("applyVoucherBtn");
    if (btn) btn.addEventListener("click", applyVoucher);
});

function updateQuantityAjax(input) {
    const productId = input.getAttribute("data-product-id");
    const quantity = input.value;
    const params = new URLSearchParams();
    params.append("action", "update");
    params.append("productId", productId);
    params.append("quantity", quantity);
    fetch(window.contextPath + '/cart', {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            "X-Requested-With": "XMLHttpRequest"
        },
        body: params.toString()
    }).then(response => {
        if (response.ok) {
            fetchCart();
        }
    }).catch(error => console.error("Lỗi cập nhật số lượng: ", error));
}

//js load thông báo lỗi
document.addEventListener("DOMContentLoaded", function (){
    const msgElement = document.getElementById("sessionMsg");
    if(msgElement){
        const content = msgElement.getAttribute('data-content')
        if(typeof Swaf !== 'undefined'){
            Swaf.fire({
                icon: 'warning',
                title: 'Thông báo',
                text: content,
                confirmButtonText: 'Đã hiểu'
            })
        }
        else {
            alert(content);
        }
    }
})