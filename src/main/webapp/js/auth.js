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
        // Sử dụng đường dẫn tương đối từ gốc domain
        // Nếu project của bạn là 'juicy_war', link sẽ là /juicy_war/logout
        const contextPath = window.location.pathname.split('/')[1];
        window.location.href = `/${contextPath}/logout`;
    }
}
//ktra flag lấy từ jsp cho viec hiện modal
document.addEventListener("DOMContentLoaded", function() {
    // 1. Kiểm tra xem trên trang có cái thẻ "đánh dấu" triggerModalFlag không
    const flag = document.getElementById('triggerModalFlag');

    if (flag && flag.value === 'true') {
        // 2. Nếu có, tìm Modal và mở nó lên
        const modalElement = document.getElementById('checkoutModal');

        if (modalElement) {
            const myModal = new bootstrap.Modal(modalElement);
            myModal.show();
        }
    }
});