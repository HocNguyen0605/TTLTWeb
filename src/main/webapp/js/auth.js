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