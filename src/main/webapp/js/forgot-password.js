/**
 * Setup Forgot Password submission logic.
 *
 * @param {string} formId - ID of the forgot password form
 * @param {string} emailInputId - ID of the email input field
 * @param {string} msgDivId - ID of the div to show messages
 * @param {string} btnId - ID of the submit button
 * @param {string} contextPath - web app context path
 */
function setupForgotPasswordLogic(formId, emailInputId, msgDivId, btnId, contextPath) {
    const form = document.getElementById(formId);
    if (!form) return;

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const email = document.getElementById(emailInputId).value;
        const msgDiv = document.getElementById(msgDivId);
        const btn = document.getElementById(btnId);

        msgDiv.innerHTML = "Đang xử lý...";
        btn.disabled = true;

        fetch(contextPath + '/forgot-password', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'email=' + encodeURIComponent(email)
        })
            .then(response => response.text())
            .then(data => {
                if (data === "success") {
                    msgDiv.className = "mt-2 small text-success";
                    msgDiv.innerHTML = "Mật khẩu mới đã được gửi vào Email của bạn!";
                } else if (data === "not_found") {
                    msgDiv.className = "mt-2 small text-danger";
                    msgDiv.innerHTML = "Email này không tồn tại trong hệ thống!";
                } else {
                    msgDiv.className = "mt-2 small text-danger";
                    msgDiv.innerHTML = "Có lỗi xảy ra, vui lòng thử lại!";
                }
                btn.disabled = false;
            });
    });
}
