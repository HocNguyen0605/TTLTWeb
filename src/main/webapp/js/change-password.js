export function initChangePassword() {
    const form = document.getElementById('formChangePassword');
    if (!form) return;

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const formData = new URLSearchParams(new FormData(form));
        const notice = document.getElementById('passwordFeedback');

        // Reset thông báo cũ
        document.querySelectorAll('[id$="Error"]').forEach(el => {
            el.innerHTML = '';
        });
        document.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));
        if (notice) notice.className = 'alert d-none';

        fetch(form.action, {
            method: 'POST',
            body: formData,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        })
            .then(async res => {
                const data = await res.json();

                if (res.ok) {
                    if (notice) {
                        notice.innerHTML = data.message;
                        notice.className = 'alert alert-success d-block';
                    }
                    form.reset();
                } else {
                    const errors = data.errors || {};

                    // Chỉ xóa mk cũ
                    if (errors.oldPassword) {
                        const oldInput = document.getElementById('oldPassword');
                        oldInput.value = '';
                        oldInput.classList.add('is-invalid');
                        document.getElementById('oldPasswordError').innerHTML = errors.oldPassword;
                        oldInput.focus();
                    }

                    // MK confirm ko khớp xóa ô new và confirm
                    if (errors.confirmPassword) {
                        const newIn = document.getElementById('newPassword');
                        const confIn = document.getElementById('confirmPassword');
                        newIn.value = '';
                        confIn.value = '';
                        newIn.classList.add('is-invalid');
                        confIn.classList.add('is-invalid');
                        document.getElementById('confirmPasswordError').innerHTML = errors.confirmPassword;
                        confIn.focus();
                    }

                    // Giữ nguyên
                    if (errors.newPassword) {
                        const newIn = document.getElementById('newPassword');
                        const confIn = document.getElementById('confirmPassword');
                        if (errors.newPassword.includes("giống mật khẩu cũ")) {
                            newIn.value = '';
                            confIn.value = '';
                        }
                        newIn.classList.add('is-invalid');
                        document.getElementById('newPasswordError').innerHTML = errors.newPassword;
                        newIn.focus();
                    }
                }
            })
            .catch(err => {
                console.error(err);
                if (notice) {
                    notice.innerHTML = 'Lỗi hệ thống, vui lòng thử lại.';
                    notice.className = 'alert alert-danger d-block';
                }
            });
    });
}