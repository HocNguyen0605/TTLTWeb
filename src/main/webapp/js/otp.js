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
        const emailRegex = /^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,20}$/;
        const emailValue = emailInput.value.trim();

        if (!emailValue) {
            notice.innerHTML = "Vui lòng nhập email trước";
            return;
        } else if (!emailRegex.test(emailValue)) {
            notice.innerHTML = "Vui lòng nhập email đúng định dạng";
            return;
        }

        btn.disabled = true;
        notice.innerHTML = "Đang gửi mã...";
        notice.style.color = "black";

        fetch(contextPath + '/send-otp?email=' + encodeURIComponent(emailValue))
            .then(res => res.json())
            .then(data => {
                if (data.status === 'success') {
                    notice.style.color = "green";

                    let interval;
                    let timeLeft = 60;

                    const updateTimer = () => {
                        if (timeLeft <= 0) {
                            clearInterval(interval);
                            btn.disabled = false;
                            notice.innerHTML = "Mã đã hết hạn, vui lòng gửi lại";
                            notice.style.color = "red";
                        } else {
                            notice.innerHTML = "Mã hiệu lực trong: " + timeLeft + "s";
                            timeLeft -= 1;
                        }
                    };

                    updateTimer();
                    interval = setInterval(updateTimer, 1000);
                } else {
                    btn.disabled = false;
                    notice.innerHTML = data.message;
                    notice.style.color = "red";
                }
            })
            .catch(err => {
                btn.disabled = false;
                notice.innerHTML = "Lỗi kết nối máy chủ.";
                notice.style.color = "red";
            });
    });
}
