// Xem trước ảnh ngay khi chọn file
function previewImage(input) {
    const preview = document.querySelector('#imagePreview');
    const img = preview.querySelector('img');

    if (input.files && input.files[0]) {
        const reader = new FileReader();

        reader.onload = function(e) {
            img.src = e.target.result;
            preview.classList.remove('d-none');
        }

        reader.readAsDataURL(input.files[0]);
    }
}
document.addEventListener("DOMContentLoaded", function (){
    const updateModal = document.getElementById('updateBannerModal');
    if (updateModal) {
        updateModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            console.log(">>> Đã bấm nút Sửa thành công!");
            const id = button.getAttribute('data-id');
            const title = button.getAttribute('data-title');
            const imageUrl = button.getAttribute('data-image');
            const linkUrl = button.getAttribute('data-link');
            const priority = button.getAttribute('data-priority');
            const isActive = button.getAttribute('data-active');

            document.getElementById('edit_id').value = id;
            document.getElementById('edit_title').value = title;
            document.getElementById('edit_link_url').value = linkUrl;
            document.getElementById('edit_priority').value = priority;
            document.getElementById('edit_is_active').value = isActive;

            const previewOld = document.getElementById('edit_preview_old');
            if (imageUrl) {
                previewOld.src = imageUrl;
                previewOld.style.display = "block";
            } else {
                previewOld.src = "";
            }
        });
    }
});