document.addEventListener("DOMContentLoaded", () => {
    // Helper to get base path
    const getBasePath = () => {
        const scriptTag = document.querySelector('script[src*="myorders.js"]');
        if (scriptTag) {
            const src = scriptTag.getAttribute('src');
            const idx = src.indexOf('/js/myorders.js');
            return idx >= 0 ? src.substring(0, idx) : '';
        }
        return '';
    };

    //HỦY ĐƠN HÀNG
    const cancelModalElement = document.getElementById('cancelOrderModal');
    if (cancelModalElement) {
        const cancelModal = new bootstrap.Modal(cancelModalElement);
        const cancelOrderForm = document.getElementById('cancelOrderForm');
        const cancelOrderIdInput = document.getElementById('cancelOrderIdInput');
        const cancelOrderIdDisplay = document.getElementById('cancelOrderIdDisplay');
        const btnSubmitCancel = document.getElementById('btnSubmitCancel');

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
            if (reason.length < 5) {
                if (typeof Swal !== 'undefined') Swal.fire('Lỗi', "Vui lòng nhập lý do hủy đơn ít nhất 5 ký tự.", 'error');
                else alert("Vui lòng nhập lý do hủy đơn ít nhất 5 ký tự.");
                return;
            }

            const orderId = cancelOrderIdInput.value;
            const formData = new URLSearchParams();
            formData.append('orderId', orderId);
            formData.append('reason', reason);

            btnSubmitCancel.disabled = true;
            btnSubmitCancel.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Đang xử lý...';

            try {
                const response = await fetch(`${getBasePath()}/user/cancel-order`, {
                    method: 'POST',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded', 'Accept': 'application/json'},
                    body: formData.toString()
                });

                const data = await response.json();
                if (response.ok && data.status === 'success') {
                    updateOrderUIToCancelled(orderId);
                    cancelModal.hide();
                    if (typeof Swal !== 'undefined') {
                        Swal.fire({
                            icon: 'success',
                            title: 'Thành công!',
                            text: data.message || "Hủy đơn hàng thành công!",
                            timer: 2000,
                            showConfirmButton: false
                        });
                    } else alert(data.message || "Hủy đơn hàng thành công!");
                } else {
                    if (typeof Swal !== 'undefined') {
                        Swal.fire('Lỗi', data.message || "Có lỗi xảy ra khi hủy đơn.", 'error');
                    } else alert(data.message || "Có lỗi xảy ra khi hủy đơn.");
                }
            } catch (error) {
                console.error('Error cancelling order:', error);
                if (typeof Swal !== 'undefined') {
                    Swal.fire('Lỗi', "Lỗi kết nối đến máy chủ.", 'error');
                } else alert("Lỗi kết nối đến máy chủ.");
            } finally {
                btnSubmitCancel.disabled = false;
                btnSubmitCancel.textContent = 'Xác nhận hủy';
            }
        });
    }

    //MUA LẠI ĐƠN HÀNG
    document.addEventListener('click', async (event) => {
        const reorderBtn = event.target.closest('.btn-reorder');
        if (reorderBtn) {
            const orderId = reorderBtn.dataset.orderId;
            reorderBtn.disabled = true;
            const originalHtml = reorderBtn.innerHTML;
            reorderBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Đang xử lý...';

            const formData = new URLSearchParams();
            formData.append('orderId', orderId);

            try {
                const response = await fetch(`${getBasePath()}/user/reorder`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded', 'Accept': 'application/json' },
                    body: formData.toString()
                });

                const data = await response.json();
                if (response.ok && data.status === 'success') {
                    if (typeof Swal !== 'undefined') {
                        Swal.fire({ icon: 'success', title: 'Thành công!', text: data.message, timer: 1000, showConfirmButton: false }).then(() => {
                            window.location.href = getBasePath() + '/cart';
                        });
                    } else {
                        alert(data.message);
                        window.location.href = getBasePath() + '/cart';
                    }
                } else {
                    if (typeof Swal !== 'undefined') Swal.fire('Lỗi', data.message || "Có lỗi xảy ra.", 'error');
                    else alert(data.message || "Có lỗi xảy ra.");
                    reorderBtn.disabled = false;
                    reorderBtn.innerHTML = originalHtml;
                }
            } catch (error) {
                console.error('Error reordering:', error);
                if (typeof Swal !== 'undefined') Swal.fire('Lỗi', "Lỗi kết nối đến máy chủ.", 'error');
                else alert("Lỗi kết nối đến máy chủ.");
                reorderBtn.disabled = false;
                reorderBtn.innerHTML = originalHtml;
            }
        }
    });

    //ĐÁNH GIÁ SẢN PHẨM
    const reviewModalElement = document.getElementById('reviewModal');
    if (reviewModalElement) {
        const reviewForm = document.getElementById('reviewForm');
        const btnSubmitReview = document.getElementById('btnSubmitReview');

        // Bắt sự kiện khi modal sắp hiển thị để đổ dữ liệu
        reviewModalElement.addEventListener('show.bs.modal', (event) => {
            const button = event.relatedTarget; // Nút đã kích hoạt modal
            const productId = button.getAttribute('data-product-id');
            const productName = button.getAttribute('data-product-name');

            document.getElementById('reviewProductId').value = productId;
            document.getElementById('reviewProductName').textContent = productName;
            document.getElementById('reviewContent').value = '';
            document.getElementById('star5').checked = true;
        });

        if (reviewForm) {
            reviewForm.addEventListener('submit', async (event) => {
                event.preventDefault();
                const productId = document.getElementById('reviewProductId').value;
                const rating = reviewForm.querySelector('input[name="rating"]:checked').value;
                const content = document.getElementById('reviewContent').value.trim();

                if (!content) {
                    if (typeof Swal !== 'undefined') Swal.fire('Lỗi', 'Vui lòng nhập nội dung đánh giá', 'error');
                    else alert('Vui lòng nhập nội dung đánh giá');
                    return;
                }

                const formData = new URLSearchParams();
                formData.append('productId', productId);
                formData.append('rating', rating);
                formData.append('content', content);

                btnSubmitReview.disabled = true;
                btnSubmitReview.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Đang gửi...';

                try {
                    const response = await fetch(`${getBasePath()}/submit-review`, {
                        method: 'POST',
                        headers: {'Content-Type': 'application/x-www-form-urlencoded', 'Accept': 'application/json'},
                        body: formData.toString()
                    });

                    const data = await response.json();
                    if (data.status === 'success') {
                        if (typeof Swal !== 'undefined') {
                            Swal.fire({
                                icon: 'success',
                                title: 'Thành công!',
                                text: data.message,
                                timer: 2000,
                                showConfirmButton: false
                            });
                        } else alert(data.message);

                        bootstrap.Modal.getInstance(reviewModalElement).hide();

                        // Cập nhật nút ngoài giao diện
                        const buttons = document.querySelectorAll(`.review-btn[data-product-id="${productId}"]`);
                        buttons.forEach(btn => {
                            btn.disabled = true;
                            btn.removeAttribute('data-bs-toggle'); // Gỡ bỏ toggle để không mở lại modal
                            btn.innerHTML = '<i class="bi bi-check-circle me-1"></i>Đã đánh giá';
                            btn.classList.replace('btn-primary', 'btn-outline-secondary');
                        });
                    } else {
                        if (typeof Swal !== 'undefined') Swal.fire('Lỗi', data.message, 'error');
                        else alert(data.message);
                    }
                } catch (error) {
                    console.error('Error submitting review:', error);
                    alert("Đã xảy ra lỗi khi gửi đánh giá.");
                } finally {
                    btnSubmitReview.disabled = false;
                    btnSubmitReview.textContent = 'Gửi đánh giá';
                }
            });
        }
    }

    function updateOrderUIToCancelled(orderId) {
        const cancelBtn = document.querySelector(`.btn-cancel-order[data-order-id="${orderId}"]`);
        if (!cancelBtn) return;
        const orderCard = cancelBtn.closest('.order-card');
        if (!orderCard) return;
        const statusPill = orderCard.querySelector('.status-pill');
        if (statusPill) {
            statusPill.className = 'status-pill status-cancelled';
            statusPill.textContent = 'cancelled';
        }
        const actionContainer = orderCard.querySelector('.order-actions');
        if (actionContainer) {
            cancelBtn.remove();
            const reorderBtn = document.createElement('button');
            reorderBtn.className = 'btn btn-warning btn-sm';
            reorderBtn.disabled = true;
            reorderBtn.innerHTML = '<i class="bi bi-arrow-repeat me-1"></i>Mua lại';
            actionContainer.appendChild(reorderBtn);
        }
    }
});
