<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Xác minh Bảo mật - Pro Admin</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo/logo-juicy.png">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <style>
    body {
      background-color: #f8f9fa;
    }
    .otp-input {
      width: 50px;
      height: 60px;
      font-size: 24px;
      text-align: center;
      margin: 0 5px;
      border: 2px solid #ddd;
      border-radius: 8px;
      font-weight: bold;
    }
    .otp-input:focus {
      border-color: #198754;
      box-shadow: 0 0 0 0.2rem rgba(25, 135, 84, 0.25);
      outline: none;
    }
    .otp-container {
      display: flex;
      justify-content: center;
      margin-bottom: 20px;
    }
    /* Ẩn mũi tên của input type number */
    .otp-input::-webkit-outer-spin-button,
    .otp-input::-webkit-inner-spin-button {
      -webkit-appearance: none;
      margin: 0;
    }
    .otp-input[type=number] {
      -moz-appearance: textfield;
    }
  </style>
</head>
<body class="d-flex align-items-center justify-content-center vh-100">

<div class="card shadow-lg border-0" style="max-width: 450px; width: 100%; border-radius: 15px;">
  <div class="card-body p-5 text-center">
    <div class="mb-4">
      <i class="bi bi-shield-lock text-success" style="font-size: 3rem;"></i>
    </div>
    <h3 class="fw-bold mb-3 text-success">Xác minh Danh tính</h3>
    <p class="text-muted mb-4">Bạn đang truy cập vào khu vực Quản lý Tài khoản (cấp độ Pro-Admin). Vui lòng xác minh mã OTP gửi về Email của bạn để tiếp tục.</p>

    <div id="alertBox" class="alert d-none" role="alert"></div>

    <button id="btnSendOTP" class="btn btn-outline-success rounded-pill mb-4 px-4 w-100">
      <i class="bi bi-envelope"></i> Gửi mã OTP ngay
    </button>

    <div id="otpSection" class="d-none">
      <div class="otp-container">
        <input type="number" class="otp-input" maxlength="1" oninput="moveToNext(this, event)">
        <input type="number" class="otp-input" maxlength="1" oninput="moveToNext(this, event)">
        <input type="number" class="otp-input" maxlength="1" oninput="moveToNext(this, event)">
        <input type="number" class="otp-input" maxlength="1" oninput="moveToNext(this, event)">
        <input type="number" class="otp-input" maxlength="1" oninput="moveToNext(this, event)">
        <input type="number" class="otp-input" maxlength="1" oninput="moveToNext(this, event)">
      </div>

      <button id="btnVerify" class="btn btn-success rounded-pill w-100 fw-bold py-2 mb-3">
        <span id="spinner" class="spinner-border spinner-border-sm d-none me-2" role="status"></span>
        Xác nhận & Tiếp tục
      </button>

      <div class="text-center">
        <small class="text-muted">Chưa nhận được mã? <a href="#" id="btnResend" class="text-success text-decoration-none fw-bold">Gửi lại (<span id="countdown">60</span>s)</a></small>
      </div>
    </div>

    <div class="mt-4">
      <a href="${pageContext.request.contextPath}/admin/dashboard" class="text-muted text-decoration-none">
        <i class="bi bi-arrow-left"></i> Quay lại Dashboard
      </a>
    </div>
  </div>
</div>

<script>
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

    fetch('${pageContext.request.contextPath}/admin/verify-pro', {
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

    fetch('${pageContext.request.contextPath}/admin/verify-pro', {
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
</script>

</body>
</html>
