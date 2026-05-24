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
                            timer: 1500,
                            showConfirmButton: false
                        }).then(() => {
                            window.location.reload();
                        });
                    } else {
                        alert(data.message || "Hủy đơn hàng thành công!");
                        window.location.reload();
                    }
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

    // YÊU CẦU TRẢ HÀNG / HOÀN TIỀN
    const refundModalElement = document.getElementById('refundOrderModal');
    if (refundModalElement) {
        const refundModal = new bootstrap.Modal(refundModalElement);
        const refundForm = document.getElementById('refundForm');
        const refundOrderIdInput = document.getElementById('refundOrderIdInput');
        const refundOrderIdDisplay = document.getElementById('refundOrderIdDisplay');
        const btnSubmitRefund = document.getElementById('btnSubmitRefund');

        // Mở modal khi click
        document.addEventListener('click', (event) => {
            const refundBtn = event.target.closest('.request-refund-btn');
            if (refundBtn) {
                const orderId = refundBtn.dataset.orderId;
                refundOrderIdInput.value = orderId;
                refundOrderIdDisplay.textContent = '#' + orderId;
                refundForm.reset();
                refundForm.classList.remove('was-validated');
                refundModal.show();
            }
        });

        // Xử lí form
        refundForm.addEventListener('submit', async (event) => {
            event.preventDefault();
            if (!refundForm.checkValidity()) {
                event.stopPropagation();
                refundForm.classList.add('was-validated');
                return;
            }
            //Kiểm tra input
            const reason = document.getElementById('refundReason').value.trim();
            if (reason.length < 5) {
                if (typeof Swal !== 'undefined') Swal.fire('Lỗi', 'Vui lòng nhập lý do ít nhất 5 ký tự.', 'error');
                else alert('Vui lòng nhập lý do ít nhất 5 ký tự.');
                return;
            }
            //Điền orderID vào form
            const orderId = refundOrderIdInput.value;
            const formData = new URLSearchParams();
            formData.append('orderId', orderId);
            formData.append('reason', reason);

            btnSubmitRefund.disabled = true;
            btnSubmitRefund.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Đang xử lý...';

            //Xử lí status data trả về thông báo hệ thống
            try {
                const response = await fetch(`${getBasePath()}/user/requestRefund`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded', 'Accept': 'application/json' },
                    body: formData.toString()
                });

                const data = await response.json();
                if (response.ok && data.status === 'success') {
                    refundModal.hide();
                    if (typeof Swal !== 'undefined') {
                        Swal.fire({
                            icon: 'success',
                            title: 'Thành công!',
                            text: data.message || 'Yêu cầu hoàn tiền đã được ghi nhận.',
                            timer: 2000,
                            showConfirmButton: false
                        }).then(() => { window.location.reload(); });
                    } else {
                        alert(data.message || 'Yêu cầu hoàn tiền đã được ghi nhận.');
                        window.location.reload();
                    }
                } else {
                    if (typeof Swal !== 'undefined') Swal.fire('Lỗi', data.message || 'Có lỗi xảy ra.', 'error');
                    else alert(data.message || 'Có lỗi xảy ra.');
                }
            } catch (error) {
                console.error('Error requesting refund:', error);
                if (typeof Swal !== 'undefined') Swal.fire('Lỗi', 'Lỗi kết nối đến máy chủ.', 'error');
                else alert('Lỗi kết nối đến máy chủ.');
            } finally {
                btnSubmitRefund.disabled = false;
                btnSubmitRefund.textContent = 'Gửi yêu cầu';
            }
        });
    }

    function updateOrderUIToCancelled(orderId) {
        const cancelBtns = document.querySelectorAll(`.btn-cancel-order[data-order-id="${orderId}"]`);
        cancelBtns.forEach(cancelBtn => {
            const orderCard = cancelBtn.closest('.order-card');
            if (!orderCard) return;
            const statusPill = orderCard.querySelector('.status-pill');
            if (statusPill) {
                statusPill.className = 'status-pill status-cancelled text-danger';
                statusPill.textContent = 'ĐÃ HỦY';
            }
            const actionContainer = orderCard.querySelector('.order-actions');
            if (actionContainer) {
                cancelBtn.remove();
                const reorderBtn = document.createElement('button');
                reorderBtn.className = 'btn btn-warning btn-sm btn-reorder text-white';
                reorderBtn.setAttribute('data-order-id', orderId);
                reorderBtn.innerHTML = '<i class="bi bi-arrow-repeat me-1"></i>Mua lại';
                actionContainer.appendChild(reorderBtn);
            }
        });
    }

    // Search bar
    const searchInput = document.getElementById('orderSearchInput');
    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            const query = e.target.value.toLowerCase().trim();
            const activePane = document.querySelector('.tab-pane.active');
            if (!activePane) return;

            const cards = activePane.querySelectorAll('.order-card');
            let visibleCount = 0;

            cards.forEach(card => {
                const orderId = card.getAttribute('data-order-id') || '';
                const productTitles = Array.from(card.querySelectorAll('.order-items-preview .fw-bold')).map(el => el.textContent.toLowerCase());

                const matchesId = orderId.includes(query);
                const matchesProducts = productTitles.some(title => title.includes(query));

                if (query === '' || matchesId || matchesProducts) {
                    card.style.display = '';
                    visibleCount++;
                } else {
                    card.style.display = 'none';
                }
            });

            // Hiện/Ẩn trạng thái rỗng của tab hiện tại nếu tìm kiếm không ra kết quả
            let emptyState = activePane.querySelector('.empty-tab-state');
            if (!emptyState && visibleCount === 0) {
                // Tạo một empty state động nếu chưa có
                emptyState = document.createElement('div');
                emptyState.className = 'text-center py-5 empty-tab-state dynamic-empty';
                emptyState.innerHTML = `
                    <i class="bi bi-search text-muted" style="font-size: 3rem; opacity: 0.4;"></i>
                    <p class="text-muted mt-2 mb-0">Không tìm thấy đơn hàng nào phù hợp</p>
                `;
                activePane.appendChild(emptyState);
            } else if (emptyState && visibleCount > 0) {
                // Xoá empty state động hoặc ẩn đi
                if (emptyState.classList.contains('dynamic-empty')) {
                    emptyState.remove();
                } else {
                    emptyState.style.display = 'none';
                }
            } else if (emptyState && visibleCount === 0) {
                emptyState.style.display = '';
            }
        });
    }
});
