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
                    headers: {'Content-Type': 'application/x-www-form-urlencoded', 'Accept': 'application/json'},
                    body: formData.toString()
                });

                const data = await response.json();
                if (response.ok && data.status === 'success') {
                    const redirectUrl = getBasePath() + '/cart' + (data.productIds ? '?listIdSelected=' + data.productIds : '');
                    if (typeof Swal !== 'undefined') {
                        Swal.fire({
                            icon: 'success',
                            title: 'Thành công!',
                            text: data.message,
                            timer: 1000,
                            showConfirmButton: false
                        }).then(() => {
                            window.location.href = redirectUrl;
                        });
                    } else {
                        alert(data.message);
                        window.location.href = redirectUrl;
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
            const button = event.relatedTarget ? event.relatedTarget.closest('.review-btn') : null;
            if (!button) return;
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
                        const reviewModal = bootstrap.Modal.getInstance(reviewModalElement) || bootstrap.Modal.getOrCreateInstance(reviewModalElement);
                        reviewModal.hide();

                        if (typeof Swal !== 'undefined') {
                            Swal.fire({
                                icon: 'success',
                                title: 'Thành công!',
                                text: data.message,
                                timer: 2000,
                                showConfirmButton: false
                            }).then(() => {
                                window.location.reload();
                            });
                        } else {
                            alert(data.message);
                            window.location.reload();
                        }
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
                    headers: {'Content-Type': 'application/x-www-form-urlencoded', 'Accept': 'application/json'},
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
                        }).then(() => {
                            window.location.reload();
                        });
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

    // THEO DÕI ĐƠN HÀNG
    const trackOrderModalElement = document.getElementById('trackOrderModal');
    if (trackOrderModalElement) {
        const trackModal = new bootstrap.Modal(trackOrderModalElement);
        const trackOrderIdDisplay = document.getElementById('trackOrderIdDisplay');
        const trackingTimelineContainer = document.getElementById('trackingTimelineContainer');

        document.addEventListener('click', async (event) => {
            const trackBtn = event.target.closest('.btn-track-order');
            if (trackBtn) {
                const orderId = trackBtn.dataset.orderId;
                trackOrderIdDisplay.textContent = "#" + orderId;
                
                trackingTimelineContainer.innerHTML = `
                    <div class="text-center text-muted py-4" id="trackingLoading">
                        <div class="spinner-border text-info" role="status">
                            <span class="visually-hidden">Loading...</span>
                        </div>
                        <p class="mt-2">Đang tải dữ liệu...</p>
                    </div>
                `;
                trackModal.show();

                try {
                    const response = await fetch(`${getBasePath()}/user/order-tracking?orderId=${orderId}`);
                    const data = await response.json();
                    
                    if (data.status === 'error') {
                        trackingTimelineContainer.innerHTML = `<div class="alert alert-warning">${data.message}</div>`;
                    } else if (data.data && data.data.log && data.data.log.length > 0) {
                        let html = '<ul class="timeline">';
                        // Sort log by updated_date descending
                        const logs = data.data.log.sort((a, b) => new Date(b.updated_date) - new Date(a.updated_date));
                        
                        logs.forEach((item, index) => {
                            const dateObj = new Date(item.updated_date);
                            const dateStr = dateObj.toLocaleString('vi-VN', {
                                year: 'numeric', month: '2-digit', day: '2-digit', 
                                hour: '2-digit', minute:'2-digit'
                            });
                            
                            // Map status in GHN to Vietnamese
                            let statusText = item.status;
                            if (statusText === 'ready_to_pick') statusText = 'Chờ lấy hàng';
                            else if (statusText === 'picking') statusText = 'Đang lấy hàng';
                            else if (statusText === 'picked') statusText = 'Đã lấy hàng';
                            else if (statusText === 'storing') statusText = 'Lưu kho';
                            else if (statusText === 'transporting') statusText = 'Đang luân chuyển';
                            else if (statusText === 'delivering') statusText = 'Đang giao hàng';
                            else if (statusText === 'delivered') statusText = 'Giao hàng thành công';
                            else if (statusText === 'delivery_fail') statusText = 'Giao hàng thất bại';
                            else if (statusText === 'return') statusText = 'Đang hoàn hàng';
                            else if (statusText === 'returned') statusText = 'Đã hoàn hàng';
                            else if (statusText === 'cancel') statusText = 'Đã hủy đơn';
                            
                            const isCompleted = index === 0 ? 'completed' : '';
                            html += `
                                <li class="timeline-item ${isCompleted}">
                                    <div class="timeline-date">${dateStr}</div>
                                    <div class="timeline-content">${statusText}</div>
                                </li>
                            `;
                        });
                        html += '</ul>';
                        trackingTimelineContainer.innerHTML = html;
                    } else {
                        trackingTimelineContainer.innerHTML = `<div class="alert alert-info">Chưa có thông tin cập nhật cho đơn hàng này.</div>`;
                    }
                } catch (error) {
                    console.error('Error fetching tracking info:', error);
                    trackingTimelineContainer.innerHTML = `<div class="alert alert-danger">Lỗi kết nối đến máy chủ. Không thể lấy thông tin vận chuyển.</div>`;
                }
            }
        });
    }

});
