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