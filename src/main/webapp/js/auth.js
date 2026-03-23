// Hàm ẩn/hiện nút Đăng Nhập và Thông Tin
export function updateAuthUI(isLoggedIn) {
    const loginBtnContainer = document.getElementById('loginButtonContainer');
    const userBtnContainer = document.getElementById('userInfoContainer');

    if (loginBtnContainer && userBtnContainer) {
        if (isLoggedIn) {
            loginBtnContainer.classList.add('d-none');
            userBtnContainer.classList.remove('d-none');
        } else {
            loginBtnContainer.classList.remove('d-none');
            userBtnContainer.classList.add('d-none');
        }
    }
}

export function handleLogout() {
    if (confirm("Bạn có chắc chắn muốn đăng xuất không?")) {
        const contextPath = window.location.pathname.split('/')[1];
        window.location.href = `/${contextPath}/logout`;
    }
}
//kt flag lấy từ jsp cho viec hiện modal
document.addEventListener("DOMContentLoaded", function() {
    const flag = document.getElementById('triggerModalFlag');

    if (flag && flag.value === 'true') {
        const modalElement = document.getElementById('checkoutModal');

        if (modalElement) {
            const myModal = new bootstrap.Modal(modalElement);
            myModal.show();
        }
    }
});