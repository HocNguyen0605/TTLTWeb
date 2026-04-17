/**
 * Setup OTP sending logic for a button.
 *
 * @param {string} btnId - ID of the button that triggers OTP send
 * @param {string} emailInputSelector - CSS selector for the email input
 * @param {string} noticeId - ID of the span/div to show notice
 * @param {string} contextPath - web app context path
 */
function setupOTPSender(btnId, emailInputSelector, noticeId, contextPath) {
    const btn = document.getElementById(btnId);
    if (!btn) return;

    btn.addEventListener('click', function () {
        const emailInput = document.querySelector(emailInputSelector);
        const notice = document.getElementById(noticeId);

        if (!emailInput || !emailInput.value) {
            notice.innerHTML = "Vui lòng nhập email trước";
            return;
        }

        const email = emailInput.value;
        let interval;
        let timeLeft = 60;

        btn.disabled = true;
        notice.innerHTML = "Đang gửi mã... (60s)";

        const updateTimer = () => {
            if (timeLeft <= 0) {
                clearInterval(interval);
                btn.disabled = false;
                notice.innerHTML = "Mã đã hết hạn, vui lòng gửi lại";
            } else {
                notice.innerHTML = "Mã hiệu lực trong: " + timeLeft + "s";
                timeLeft -= 1;
            }
        };

        updateTimer();
        interval = setInterval(updateTimer, 1000);

        fetch(contextPath + '/send-otp?email=' + encodeURIComponent(email))
            .then(res => res.text())
            .then(data => {
                if (data !== 'success') {
                    throw new Error('Failed');
                }
            })
            .catch(err => {
                notice.innerHTML = "Lỗi gửi mã, thử lại sau.";
                btn.disabled = false;
                clearInterval(interval);
            });
    });
}
