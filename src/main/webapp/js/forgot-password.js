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
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'Accept': 'application/json'
            },
            body: 'email=' + encodeURIComponent(email)
        })
            .then(response => response.json())
            .then(data => {
                if (data.status === "success") {
                    msgDiv.className = "mt-2 small text-success";
                    msgDiv.innerHTML = data.message;
                } else {
                    msgDiv.className = "mt-2 small text-danger";
                    msgDiv.innerHTML = data.message || "Có lỗi xảy ra, vui lòng thử lại!";
                }
                btn.disabled = false;
            })
            .catch(err => {
                console.error(err);
                msgDiv.className = "mt-2 small text-danger";
                msgDiv.innerHTML = "Lỗi kết nối, vui lòng thử lại sau.";
                btn.disabled = false;
            });
    });
}
