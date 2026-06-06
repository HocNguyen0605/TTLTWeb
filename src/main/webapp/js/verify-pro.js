// Logic nhập mã OTP 6 ô
function moveToNext(input, event) {
    if (input.value.length > 1) {
        input.value = input.value.slice(0, 1);
    }
    if (input.value.length === 1) {
        let next = input.nextElementSibling;
        if (next) next.focus();
    }
}

document.querySelectorAll('.otp-input').forEach((input, index) => {
    input.addEventListener('keydown', function(e) {
        if (e.key === 'Backspace' && this.value === '') {
            let prev = this.previousElementSibling;
            if (prev) {
                prev.focus();
                prev.value = '';
            }
        } else if (e.key === 'Enter') {
            if(index === 5) document.getElementById('btnVerify').click();
        }
    });

    // Hỗ trợ paste mã 6 số
    input.addEventListener('paste', function(e) {
        e.preventDefault();
        let pasteData = (e.clipboardData || window.clipboardData).getData('text');
        if (/^\d{6}$/.test(pasteData)) {
            let inputs = document.querySelectorAll('.otp-input');
            inputs.forEach((inp, i) => {
                inp.value = pasteData[i];
            });
            inputs[5].focus();
        }
    });
});

const btnSendOTP = document.getElementById('btnSendOTP');
const otpSection = document.getElementById('otpSection');
const alertBox = document.getElementById('alertBox');
const btnVerify = document.getElementById('btnVerify');
const btnResend = document.getElementById('btnResend');
const countdownSpan = document.getElementById('countdown');

function showAlert(msg, isSuccess) {
    alertBox.textContent = msg;
    alertBox.className = 'alert ' + (isSuccess ? 'alert-success' : 'alert-danger');
}

function startTimer(duration) {
    btnResend.style.pointerEvents = 'none';
    btnResend.classList.remove('text-success');
    btnResend.classList.add('text-muted');

    let timer = duration;
    let interval = setInterval(function () {
        countdownSpan.textContent = timer;
        if (--timer < 0) {
            clearInterval(interval);
            btnResend.style.pointerEvents = 'auto';
            btnResend.classList.remove('text-muted');
            btnResend.classList.add('text-success');
            countdownSpan.textContent = '0';
        }
    }, 1000);
}

function sendOTP() {
    btnSendOTP.disabled = true;
    btnSendOTP.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status"></span> Đang gửi...';

    fetch(contextPath + '/admin/verify-pro', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: 'action=sendOTP'
    })
        .then(res => res.json())
        .then(data => {
            btnSendOTP.style.display = 'none';
            if (data.status === 'success') {
                showAlert(data.message, true);
                otpSection.classList.remove('d-none');
                document.querySelectorAll('.otp-input')[0].focus();
                startTimer(60);
            } else {
                showAlert(data.message, false);
                btnSendOTP.style.display = 'block';
                btnSendOTP.disabled = false;
                btnSendOTP.innerHTML = '<i class="bi bi-envelope"></i> Gửi mã OTP ngay';
            }
        })
        .catch(err => {
            console.error(err);
            showAlert('Lỗi kết nối. Vui lòng thử lại.', false);
            btnSendOTP.style.display = 'block';
            btnSendOTP.disabled = false;
            btnSendOTP.innerHTML = '<i class="bi bi-envelope"></i> Gửi mã OTP ngay';
        });
}

btnSendOTP.addEventListener('click', sendOTP);
btnResend.addEventListener('click', function(e) {
    e.preventDefault();
    sendOTP();
});

btnVerify.addEventListener('click', function() {
    let inputs = document.querySelectorAll('.otp-input');
    let otp = '';
    inputs.forEach(inp => otp += inp.value);

    if (otp.length < 6) {
        showAlert('Vui lòng nhập đủ 6 số.', false);
        return;
    }

    btnVerify.disabled = true;
    document.getElementById('spinner').classList.remove('d-none');

    fetch(contextPath + '/admin/verify-pro', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: 'action=verify&otp=' + encodeURIComponent(otp)
    })
        .then(res => res.json())
        .then(data => {
            if (data.status === 'success') {
                showAlert('Xác minh thành công! Đang chuyển hướng...', true);
                setTimeout(() => window.location.href = data.redirect, 1000);
            } else {
                showAlert(data.message, false);
                btnVerify.disabled = false;
                document.getElementById('spinner').classList.add('d-none');
                inputs.forEach(inp => inp.value = '');
                inputs[0].focus();
            }
        })
        .catch(err => {
            console.error(err);
            showAlert('Lỗi kết nối. Vui lòng thử lại.', false);
            btnVerify.disabled = false;
            document.getElementById('spinner').classList.add('d-none');
        });
});
