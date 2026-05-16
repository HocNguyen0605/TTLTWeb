document.addEventListener("DOMContentLoaded", () => {
    const cancelModalElement = document.getElementById('cancelOrderModal');
    if (!cancelModalElement) return;

    const cancelModal = new bootstrap.Modal(cancelModalElement);

    const cancelOrderForm = document.getElementById('cancelOrderForm');
    const cancelOrderIdInput = document.getElementById('cancelOrderIdInput');
    const cancelOrderIdDisplay = document.getElementById('cancelOrderIdDisplay');
    const btnSubmitCancel = document.getElementById('btnSubmitCancel');

    // Xứ lí hành động hủy đơn
    document.addEventListener('click', (event) => {
        const cancelBtn = event.target.closest('.btn-cancel-order');
        if (cancelBtn) {
            const orderId = cancelBtn.dataset.orderId;
            cancelOrderIdInput.value = orderId;
            cancelOrderIdDisplay.textContent = "#" + orderId;
            cancelOrderForm.reset();
            cancelOrderForm.classList.remove('was-validated');
            cancelModal.show();
        }
    });

    cancelOrderForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        if (!cancelOrderForm.checkValidity()) {
            event.stopPropagation();
            cancelOrderForm.classList.add('was-validated');
            return;
        }

        const reason = document.getElementById('cancelReason').value.trim();
        if (reason.length < 10) {
            alert("Vui lòng nhập lý do hủy đơn ít nhất 10 ký tự.");
            return;
        }

        const orderId = cancelOrderIdInput.value;
        const formData = new URLSearchParams();
        formData.append('orderId', orderId);
        formData.append('reason', reason);

        btnSubmitCancel.disabled = true;
        btnSubmitCancel.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Đang xử lý...';

        try {
            const scriptTag = document.querySelector('script[src*="myorders.js"]');
            let basePath = '';
            if (scriptTag) {
                const src = scriptTag.getAttribute('src');
                basePath = src.substring(0, src.indexOf('/js/myorders.js'));
            }
            const response = await fetch(`${basePath}/user/cancel-order`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'Accept': 'application/json'
                },
                body: formData.toString()
            });

            const data = await response.json();

            if (response.ok && data.status === 'success') {
                updateOrderUIToCancelled(orderId);
                cancelModal.hide();
                alert(data.message || "Hủy đơn hàng thành công!");
            } else {
                alert(data.message || "Có lỗi xảy ra khi hủy đơn.");
            }
        } catch (error) {
            console.error('Error cancelling order:', error);
            alert("Lỗi kết nối đến máy chủ.");
        } finally {
            btnSubmitCancel.disabled = false;
            btnSubmitCancel.textContent = 'Xác nhận hủy';
        }
    });

    // Xử lí UI sau khi submit
    function updateOrderUIToCancelled(orderId) {
        const cancelBtn = document.querySelector(`.btn-cancel-order[data-order-id="${orderId}"]`);
        if (!cancelBtn) return;

        const orderCard = cancelBtn.closest('.order-card');
        if (!orderCard) return;

        const statusPill = orderCard.querySelector('.status-pill');
        if (statusPill) {
            // Remove old status classes
            statusPill.className = 'status-pill status-cancelled';
            statusPill.textContent = 'cancelled';
        }

        const actionContainer = orderCard.querySelector('.order-actions');
        if (actionContainer) {
            cancelBtn.remove();

            const reorderBtn = document.createElement('button');
            reorderBtn.className = 'btn btn-warning btn-sm';
            reorderBtn.type = 'button';
            reorderBtn.disabled = true;
            reorderBtn.innerHTML = '<i class="bi bi-arrow-repeat me-1"></i>Mua lại';

            actionContainer.appendChild(reorderBtn);
        }
    }
});
