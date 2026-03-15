//CAROUSEL LOGIC
export function initCarousel(id, interval) {
    var carousel = document.getElementById(id);
    if (!carousel) return;

    var items = carousel.querySelectorAll('.carousel-item');
    if (items.length === 0) return;
    var currentIndex = 0;
    var slideInterval;

    function showItem(index) {
        for (var i = 0; i < items.length; i++) {
            items[i].classList.remove('active');
        }
        items[index].classList.add('active');
    }

    function nextSlide() {
        currentIndex = (currentIndex + 1) % items.length;
        showItem(currentIndex);
    }

    function prevSlide() {
        currentIndex = (currentIndex - 1 + items.length) % items.length;
        showItem(currentIndex);
    }

    // 1. Tự động chuyển slide
    function startAutoSlide() {
        // Xóa interval cũ trước khi tạo mới
        clearInterval(slideInterval);
        slideInterval = setInterval(nextSlide, interval);
    }

    // 2. Thiết lập nút điều khiển
    var nextButton = carousel.querySelector('.carousel-control-next');
    var prevButton = carousel.querySelector('.carousel-control-prev');

    if (nextButton) {
        nextButton.onclick = function () {
            startAutoSlide();
            nextSlide();
        };
    }

    if (prevButton) {
        prevButton.onclick = function () {
            startAutoSlide();
            prevSlide();
        };
    }

    showItem(currentIndex);
    startAutoSlide();
}