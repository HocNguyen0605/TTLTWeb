// Tìm kiếm sản phẩm
document.addEventListener("DOMContentLoaded", function () {
    const modalSearchInput = document.getElementById('modalProductSearch');

    if (modalSearchInput) {
        modalSearchInput.addEventListener('input', function () {
            const keyword = this.value.trim().toLowerCase();

            // Lấy toàn bộ các item sản phẩm trong danh sách
            const productItems = document.querySelectorAll('#productList .product-item');

            productItems.forEach(function (item) {
                const productName = item.dataset.name ? item.dataset.name.toLowerCase() : "";

                if (productName.includes(keyword)) {
                    item.style.setProperty('display', 'flex', 'important');
                } else {
                    item.style.setProperty('display', 'none', 'important');
                }
            });
        });
    }

    // Thêm sp vào giỏ
    const cartBody = document.getElementById('cartBody');
    const cartEmpty = document.getElementById('cartEmpty');
    const cartTotal = document.getElementById('cartTotal');

    function formatCurrency(value) {
        return Math.round(value).toLocaleString('vi-VN') + ' đ';
    }

    function updateCartUI() {
        const rows = cartBody.querySelectorAll('tr');
        cartEmpty.style.display = rows.length === 0 ? '' : 'none';

        let total = 0;
        rows.forEach(row => {
            const qty = parseFloat(row.querySelector('.input-qty').value) || 0;
            const price = parseFloat(row.querySelector('.input-price').value) || 0;
            const subtotal = qty * price;
            row.querySelector('.row-subtotal').textContent = formatCurrency(subtotal);
            total += subtotal;
        });
        cartTotal.textContent = formatCurrency(total);
    }

    document.querySelectorAll('.btn-add-product').forEach(btn => {
        btn.addEventListener('click', function () {
            const item = this.closest('.product-item');
            const id = item.dataset.id;
            const name = item.dataset.name;
            const price = item.dataset.price;

            // Nếu sản phẩm đã có trong giỏ -> tăng số lượng lên 1
            const existingRow = cartBody.querySelector(`tr[data-product-id="${id}"]`);
            if (existingRow) {
                const qtyInput = existingRow.querySelector('.input-qty');
                qtyInput.value = (parseInt(qtyInput.value) || 0) + 1;
                updateCartUI();
                return;
            }

            const tr = document.createElement('tr');
            tr.setAttribute('data-product-id', id);
            tr.innerHTML = `
                <td>
                    ${name}
                    <input type="hidden" name="productId" value="${id}">
                </td>
                <td class="text-end" style="width:140px;">
                    <input type="number" step="1000" min="0" class="form-control form-control-sm text-end input-price"
                           name="importPrice" value="${price}">
                </td>
                <td class="text-center">
                    <input type="number" min="1" value="1" class="form-control form-control-sm text-center input-qty"
                           name="quantity">
                </td>
                <td class="text-end row-subtotal">${formatCurrency(price)}</td>
                <td class="text-center">
                    <button type="button" class="btn btn-sm btn-outline-danger btn-remove-row">
                        <i class="bi bi-x-lg"></i>
                    </button>
                </td>
            `;
            cartBody.appendChild(tr);
            updateCartUI();
        });
    });

    // Cập nhật tổng giá
    cartBody.addEventListener('input', function (e) {
        if (e.target.classList.contains('input-qty') || e.target.classList.contains('input-price')) {
            updateCartUI();
        }
    });

    //Xóa sản phẩm khỏi giỏ
    cartBody.addEventListener('click', function (e) {
        const btn = e.target.closest('.btn-remove-row');
        if (btn) {
            btn.closest('tr').remove();
            updateCartUI();
        }
    });

    // check thông tin trc khi gửi
    const addOrderForm = document.getElementById('addOrderForm');
    if (addOrderForm) {
        addOrderForm.addEventListener('submit', function (e) {
            if (cartBody.querySelectorAll('tr').length === 0) {
                e.preventDefault();
                alert('Vui lòng chọn ít nhất một sản phẩm.');
            }
        });
    }

    //Hiển thị thông tin đơn hàng
    const checkOutModal = document.getElementById('checkOutPurchase');
    if (checkOutModal) {
        checkOutModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const orderId = button.getAttribute('data-id');
            // Gán mã đơn hàng lên tiêu đề modal
            document.getElementById('viewOrderId').textContent = "#" + orderId;

            // Reset bảng về trạng thái đang tải
            const tbody = document.getElementById('detailTableBody');
            tbody.innerHTML = `<tr><td colspan="5" class="text-center text-muted"><div class="spinner-border spinner-border-sm text-primary me-2"></div> đang tải dữ liệu...</td></tr>`;
            const formatVND = (value) => Math.round(value).toLocaleString('vi-VN') + ' ₫';

            // Định dạng thời gian
            const formatDateTime = (timestamp) => {
                if (!timestamp) return "--/--/----";
                const d = new Date(Number(timestamp));
                if (isNaN(d.getTime())) return "--/--/----";
                return `${String(d.getDate()).padStart(2, '0')}/${String(d.getMonth() + 1).padStart(2, '0')}/${d.getFullYear()} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`;
            };

            // gửi yeu cầu lên servlet
            fetch(`${window.contextPath}/admin/purchase-orders?action=getDetailJson&id=${orderId}`)
                .then(response => {
                    if (!response.ok) throw new Error("Lỗi mạng hoặc server");
                    return response.json();
                })
                .then(data => {
                    document.getElementById('viewCreatedDate').textContent = formatDateTime(data.createdDate);
                    document.getElementById('viewTotalAmount').textContent = formatVND(data.totalAmount);

                    const statusElement = document.getElementById('viewStatus');
                    if (data.status === 'PENDING_SUPPLIER_CONFIRM') {
                        statusElement.textContent = 'Chờ NCC xác nhận';
                        statusElement.className = 'badge bg-warning text-dark';
                    } else {
                        statusElement.textContent = data.status;
                        statusElement.className = 'badge bg-success';
                    }

                    tbody.innerHTML = "";

                    if (!data.items || data.items.length === 0) {
                        tbody.innerHTML = `<tr><td colspan="5" class="text-center text-muted">Đơn hàng không có sản phẩm nào.</td></tr>`;
                        return;
                    }

                    const template = document.getElementById('orderDetailRowTemplate');
                    data.items.forEach(item => {
                        const clone = template.content.cloneNode(true);

                        const imgElement = clone.querySelector('.product-img');
                        imgElement.src = item.productImg ? item.productImg : `${window.contextPath}/images/default-product.png`;
                        imgElement.alt = item.productName;

                        clone.querySelector('.product-id').textContent = item.productId;
                        clone.querySelector('.product-name').textContent = item.productName;
                        clone.querySelector('.product-qty').textContent = item.quantity;

                        const subtotal = item.importPrice * item.quantity;
                        clone.querySelector('.product-subtotal').textContent = formatVND(subtotal);

                        tbody.appendChild(clone);
                    });
                })
                .catch(error => {
                    console.error("Lỗi AJAX:", error);
                    tbody.innerHTML = `<tr><td colspan="5" class="text-center text-danger"><i class="bi bi-x-circle-fill"></i> Không thể tải dữ liệu đơn hàng.</td></tr>`;
                });
        });
    }
});