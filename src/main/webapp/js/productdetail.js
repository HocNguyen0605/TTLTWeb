document.addEventListener("DOMContentLoaded", function () {
    const stars = document.querySelectorAll('#starRating i');
    const ratingInput = document.getElementById('ratingValue');
    const reviewForm = document.getElementById('reviewForm');
    const reviewContent = document.getElementById('reviewContent');
    const productId = CONFIG.productId;

    // 1. Logic chọn sao
    stars.forEach((star, index) => {
        star.addEventListener('click', () => {
            const val = star.getAttribute('data-value');
            ratingInput.value = val;

            stars.forEach((s, i) => {
                if (i <= index) {
                    s.classList.remove('bi-star');
                    s.classList.add('bi-star-fill');
                } else {
                    s.classList.remove('bi-star-fill');
                    s.classList.add('bi-star');
                }
            });
        });
    });

    // 2. Kiểm tra xem có review đang chờ từ localStorage không
    const pendingReview = JSON.parse(localStorage.getItem('pendingReview'));
    if (pendingReview && pendingReview.productId === productId) {
        reviewContent.value = pendingReview.content;
        ratingInput.value = pendingReview.rating;
        // Cập nhật giao diện sao
        stars.forEach((s, i) => {
            if (i < pendingReview.rating) {
                s.classList.remove('bi-star');
                s.classList.add('bi-star-fill');
            } else {
                s.classList.remove('bi-star-fill');
                s.classList.add('bi-star');
            }
        });
        localStorage.removeItem('pendingReview');

        // Nếu đã đăng nhập thì tự động kích hoạt submit
        if (CONFIG.isAuthenticated) {
            setTimeout(() => {
                reviewForm.querySelector('button[type="submit"]').click();
            }, 500);
        }
    }

    // 3. Xử lý khi nhấn Gửi Đánh Giá
    if (reviewForm) {
        reviewForm.addEventListener('submit', function (e) {
            e.preventDefault();

            if (!CONFIG.isAuthenticated) {
                // Lưu vào localStorage nếu chưa đăng nhập
                const data = {
                    productId: productId,
                    rating: ratingInput.value,
                    content: reviewContent.value
                };
                localStorage.setItem('pendingReview', JSON.stringify(data));

                // Chuyển hướng đến trang login với returnUrl
                const currentUrl = window.location.href;
                window.location.href = CONFIG.contextPath + "/login?returnUrl=" + encodeURIComponent(currentUrl);
                return;
            }

            // Nếu đã đăng nhập, gửi AJAX
            const formData = new FormData(reviewForm);
            const searchParams = new URLSearchParams(formData);

            fetch(CONFIG.contextPath + "/submit-review", {
                method: 'POST',
                body: searchParams,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })
                .then(response => response.json())
                .then(data => {
                    if (data.status === 'success') {
                        // Thông báo
                        if (typeof Swal !== 'undefined') {
                            Swal.fire({
                                icon: 'success',
                                title: 'Thành công!',
                                text: data.message,
                                timer: 2000,
                                showConfirmButton: false
                            });
                        } else {
                            alert("Thành công: " + data.message);
                        }

                        // Thêm đánh giá mới vào UI mà không reload
                        const reviewList = document.querySelector('.review-list');
                        const emptyMsg = reviewList.querySelector('.text-center.py-5');
                        if (emptyMsg) emptyMsg.remove();

                        const r = data.review;
                        const dateStr = new Date(r.createdAt).toLocaleString('vi-VN', {
                            day: '2-digit', month: '2-digit', year: 'numeric',
                            hour: '2-digit', minute: '2-digit'
                        }).replace(',', '');

                        let starsHtml = '';
                        for (let i = 1; i <= 5; i++) {
                            starsHtml += `<i class="bi bi-star${i <= r.rating ? '-fill' : ''}"></i> `;
                        }

                        const newReviewHtml = `
                            <div class="review-item mb-4 pb-3 border-bottom animate__animated animate__fadeIn">
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <span class="fw-bold text-dark">${r.userName}</span>
                                    <small class="text-muted">${dateStr}</small>
                                </div>
                                <div class="text-warning mb-2">${starsHtml}</div>
                                <p class="text-muted mb-0">${r.content}</p>
                            </div>
                        `;

                        reviewList.insertAdjacentHTML('afterbegin', newReviewHtml);
                        reviewForm.reset();
                        ratingInput.value = 5;
                        stars.forEach(s => {
                            s.classList.remove('bi-star');
                            s.classList.add('bi-star-fill');
                        });

                        // Cập nhật số lượng đánh giá trên UI
                        const countEl = document.querySelector('h6.mb-4');
                        if (countEl) {
                            const match = countEl.innerText.match(/\((\d+)\)/);
                            if (match) {
                                const newCount = parseInt(match[1]) + 1;
                                countEl.innerText = `Các lượt đánh giá (${newCount})`;
                            }
                        }
                    } else {
                        if (typeof Swal !== 'undefined') {
                            Swal.fire({
                                icon: 'error',
                                title: 'Lỗi!',
                                text: data.message
                            });
                        } else {
                            alert("Lỗi: " + data.message);
                        }
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    if (typeof Swal !== 'undefined') {
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi!',
                            text: 'Đã xảy ra lỗi khi gửi đánh giá.'
                        });
                    } else {
                        alert("Đã xảy ra lỗi khi gửi đánh giá.");
                    }
                });
        });
    }

    // 4. Logic Lọc Đánh Giá
    const filterBtns = document.querySelectorAll('#reviewFilter .filter-btn');
    const reviewItems = document.querySelectorAll('.review-item');

    filterBtns.forEach(btn => {
        btn.addEventListener('click', function () {
            filterBtns.forEach(b => b.classList.remove('active'));
            this.classList.add('active');

            const filterValue = this.getAttribute('data-filter');
            reviewItems.forEach(item => {
                if (filterValue === 'all') {
                    item.classList.remove('d-none');
                } else if (filterValue === 'has-comment') {
                    item.classList.toggle('d-none', item.getAttribute('data-has-comment') !== 'true');
                } else {
                    item.classList.toggle('d-none', item.getAttribute('data-rating') !== filterValue);
                }
            });
        });
    });
});

// 5. Logic Like Đánh Giá
function toggleLike(btn, reviewId) {
    if (!CONFIG.isAuthenticated) {
        if (typeof Swal !== 'undefined') {
            Swal.fire({
                icon: 'info',
                title: 'Yêu cầu đăng nhập',
                text: 'Bạn cần đăng nhập để thả lượt thích bình luận này!',
                confirmButtonText: 'Đăng nhập ngay',
                showCancelButton: true,
                cancelButtonText: 'Hủy'
            }).then((result) => {
                if (result.isConfirmed) {
                    const currentUrl = window.location.href;
                    window.location.href = CONFIG.contextPath + "/login?returnUrl=" + encodeURIComponent(currentUrl);
                }
            });
        } else {
            const result = confirm("Bạn cần đăng nhập để thả lượt thích bình luận này! Bạn có muốn đăng nhập không?");
            if (result) {
                const currentUrl = window.location.href;
                window.location.href = CONFIG.contextPath + "/login?returnUrl=" + encodeURIComponent(currentUrl);
            }
        }
        return;
    }

    fetch(CONFIG.contextPath + "/like-review", {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ 'reviewId': reviewId })
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                const icon = btn.querySelector('i');
                const countSpan = btn.querySelector('.like-count');
                let count = parseInt(countSpan.innerText);

                if (data.liked) {
                    btn.classList.add('liked');
                    icon.classList.remove('bi-hand-thumbs-up');
                    icon.classList.add('bi-hand-thumbs-up-fill');
                    countSpan.innerText = count + 1;
                } else {
                    btn.classList.remove('liked');
                    icon.classList.remove('bi-hand-thumbs-up-fill');
                    icon.classList.add('bi-hand-thumbs-up');
                    countSpan.innerText = count - 1;
                }
            } else {
                if (typeof Swal !== 'undefined') {
                    Swal.fire('Lỗi', data.message, 'error');
                } else {
                    alert("Lỗi: " + data.message);
                }
            }
        });
}
