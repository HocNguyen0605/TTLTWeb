export function initLogoutConfirmation(contextPath) {
    const btnLogout = document.getElementById('btnLogout');
    if (!btnLogout) return;

    btnLogout.addEventListener('click', function () {
        Swal.fire({
            title: 'Bạn muốn đăng xuất?',
            text: "Mọi phiên làm việc hiện tại sẽ kết thúc!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Đăng xuất ngay',
            cancelButtonText: 'Ở lại',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = contextPath + '/logout';
            }
        });
    });
}