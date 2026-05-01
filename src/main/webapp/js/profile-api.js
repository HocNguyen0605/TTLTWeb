export function initUpdateProfileAPI() {
    const form = document.getElementById('updateProfileForm');
    if (!form) return;

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const formData = new URLSearchParams(new FormData(form));

        // Clear previous errors
        form.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));
        form.querySelectorAll('.invalid-feedback').forEach(el => el.remove());
        const existingAlert = form.querySelector('.alert');
        if (existingAlert) existingAlert.remove();

        const container = form.closest('.profile-container');
        if (container) {
            container.querySelectorAll('.alert').forEach(el => el.remove());
        }

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

                const alertDiv = document.createElement('div');
                alertDiv.className = 'alert alert-dismissible fade show';
                alertDiv.role = 'alert';

                if (data.status === 'success') {
                    alertDiv.classList.add('alert-success');
                    alertDiv.innerHTML = `<i class="bi bi-check-circle-fill me-2"></i> ${data.message}
                                      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>`;
                    if (container) {
                        container.insertBefore(alertDiv, container.firstChild);
                    } else {
                        form.insertBefore(alertDiv, form.firstChild);
                    }
                } else {
                    if (data.errors) {
                        for (const [key, msg] of Object.entries(data.errors)) {
                            const input = form.querySelector(`input[name="${key}"]`) || form.querySelector(`textarea[name="${key}"]`);
                            if (input) {
                                input.classList.add('is-invalid');
                                const feedback = document.createElement('div');
                                feedback.className = 'invalid-feedback';
                                feedback.textContent = msg;
                                input.parentElement.appendChild(feedback);
                            }
                        }
                    }

                    if (data.message) {
                        alertDiv.classList.add('alert-danger');
                        alertDiv.innerHTML = `<i class="bi bi-exclamation-triangle-fill me-2"></i> ${data.message}
                                          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>`;
                        if (container) {
                            container.insertBefore(alertDiv, container.firstChild);
                        } else {
                            form.insertBefore(alertDiv, form.firstChild);
                        }
                    }
                }
                submitBtn.innerHTML = originalText;
                submitBtn.disabled = false;
            })
            .catch(err => {
                console.error(err);
                submitBtn.innerHTML = originalText;
                submitBtn.disabled = false;
            });
    });
}
