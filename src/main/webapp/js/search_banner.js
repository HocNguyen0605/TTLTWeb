// Lấy tên banner cần tìm kím của banner_admin
document.getElementById('searchInput').addEventListener('input', function() {
    let keyword = this.value.trim();
    let suggestionBox = document.getElementById('searchSuggestions');

    if (keyword.length > 0) {
        fetch('${pageContext.request.contextPath}/search-banner-ajax?keyword=' + keyword)
            .then(response => response.text())
            .then(data => {
                suggestionBox.innerHTML = data;
                suggestionBox.style.display = 'block';
            });
    } else {
        suggestionBox.style.display = 'none';
    }
});