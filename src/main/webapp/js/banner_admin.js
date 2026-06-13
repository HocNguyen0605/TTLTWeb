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

function previewImageUpdate(input) {
    const preview = document.querySelector('#imagePreviewUpdate');
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
            const promotionName = button.getAttribute('data-promotion-name');

            document.getElementById('edit_id').value = id;
            document.getElementById('edit_title').value = title;
            document.getElementById('edit_link_url').value = linkUrl;
            document.getElementById('edit_priority').value = priority;
            document.getElementById('edit_is_active').value = isActive;

            const selectPromo = document.getElementById('editPromotionSelect');
            const defaultOption = document.getElementById('editDefaultPromoOption');

            // Đưa về dòng mặc định ban đầu
            selectPromo.selectedIndex = 0;
            let isFound = false;

            if (promotionName && promotionName.trim() !== "" && promotionName !== "null" && promotionName !== null) {
                const cleanPromoName = promotionName.trim().toLowerCase();

                for (let i = 0; i < selectPromo.options.length; i++) {
                    let option = selectPromo.options[i];
                    let optionDataName = option.getAttribute('data-name');

                    if (optionDataName) {
                        let cleanOptionName = optionDataName.trim().toLowerCase();
                          // So sánh tên gốc với nhau
                        if (cleanOptionName === cleanPromoName) {
                            // Nhảy đến option trùng tên để hiển thị
                            selectPromo.selectedIndex = i;
                            isFound = true;
                            break;
                        }
                    }
                }
            }

            // Cập nhật chữ hiển thị động cho dòng số 0 theo đúng ngữ cảnh thực tế của banner
            if (isFound) {
                defaultOption.textContent = "Không áp dụng chương trình khuyến mãi";
            } else {
                defaultOption.textContent = "Chưa có chương trình khuyến mãi";
            }

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

console.log(">>> File banner_admin.js đã load!");