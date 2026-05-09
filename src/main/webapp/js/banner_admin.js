<!-- Xem trước ảnh ngay khi chọn file -->
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
function openEditModal(id, title, imageUrl, linkUrl, priority, isActive) {
    document.getElementById('edit_id').value = id;
    document.getElementById('edit_title').value = title;
    document.getElementById('edit_link_url').value = linkUrl;
    document.getElementById('edit_priority').value = priority;
    document.getElementById('edit_is_active').value = isActive; // Truyền 'true' hoặc 'false'

    // Hiển thị ảnh cũ để Admin xem
    const previewOld = document.getElementById('edit_preview_old');
    previewOld.src = imageUrl;

    // Ẩn phần xem trước ảnh mới (vì chưa chọn ảnh mới)
    document.getElementById('imagePreviewUpdate').classList.add('d-none');
}