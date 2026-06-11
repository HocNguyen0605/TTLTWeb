export function initLoginAPI() {
    const form = document.getElementById('loginForm');
    if (!form) return;

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const formData = new URLSearchParams(new FormData(form));

        // Xóa lỗi trước đó
        const existingAlert = form.querySelector('.alert');
        if (existingAlert) existingAlert.remove();

        const submitBtn = form.querySelector('button[type="submit"]');
        const originalText = submitBtn.innerHTML;
        submitBtn.innerHTML = 'Đang xử lý...';
        submitBtn.disabled = true;

        fetch(form.action, {
            method: 'POST',
            body: formData,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'Accept': 'application/json'
            }
        })
            .then(async res => {
                const data = await res.json();
                if (data.status === 'success') {
                    window.location.href = data.redirect;
                } else {
                    // Hiển báo lỗi
                    const alertDiv = document.createElement('div');
                    alertDiv.className = 'alert alert-danger alert-dismissible fade show d-flex align-items-center';
                    alertDiv.role = 'alert';
                    alertDiv.innerHTML = `
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                    <div>${data.message || 'Có lỗi xảy ra'}</div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                `;
                    form.insertBefore(alertDiv, form.firstChild);

                    submitBtn.innerHTML = originalText;
                    submitBtn.disabled = false;
                }
            })
            .catch(err => {
                console.error(err);
                submitBtn.innerHTML = originalText;
                submitBtn.disabled = false;
            });
    });
}

export function initRegisterAPI() {
    const form = document.getElementById('registerForm');
    if (!form) return;

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const formData = new URLSearchParams(new FormData(form));

        // xóa lỗi trước đó
        form.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));
        form.querySelectorAll('.invalid-feedback').forEach(el => el.remove());
        const existingAlert = form.querySelector('.alert');
        if (existingAlert) existingAlert.remove();

        // xoá timer
        const otpTimer = document.getElementById('otpTimer');
        if (otpTimer) otpTimer.innerHTML = '';

        const submitBtn = form.querySelector('button[type="submit"]');
        const originalText = submitBtn.innerHTML;
        submitBtn.innerHTML = 'Đang xử lý...';
        submitBtn.disabled = true;

        fetch(form.action, {
            method: 'POST',
            body: formData,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'Accept': 'application/json'
            }
        })
            .then(async res => {
                const data = await res.json();
                if (data.status === 'success') {
                    window.location.href = data.redirect;
                } else {
                    if (data.errors) {
                        for (const [key, msg] of Object.entries(data.errors)) {
                            let input;
                            if (key === 'system') {
                                // Show general error
                                const alertDiv = document.createElement('div');
                                alertDiv.className = 'alert alert-danger alert-dismissible fade show';
                                alertDiv.innerHTML = `${msg} <button type="button" class="btn-close" data-bs-dismiss="alert"></button>`;
                                form.insertBefore(alertDiv, form.firstChild);
                                continue;
                            } else if (key === 'fullname') {
                                input = form.querySelector('input[name="fullname"]');
                            } else {
                                input = form.querySelector(`input[name="${key}"]`);
                            }

                            if (input) {
                                input.classList.add('is-invalid');
                                const feedback = document.createElement('div');
                                feedback.className = 'invalid-feedback';
                                feedback.textContent = msg;

                                input.parentElement.appendChild(feedback);
                            }
                        }
                    }
                    submitBtn.innerHTML = originalText;
                    submitBtn.disabled = false;
                }
            })
            .catch(err => {
                console.error(err);
                submitBtn.innerHTML = originalText;
                submitBtn.disabled = false;
            });
    });
}
