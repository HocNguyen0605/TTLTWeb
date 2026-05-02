<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!--Thêm thanh tìm kiếm vào dưới phần header của website-->
<section class="bg-light py-4 border-bottom">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <form class="d-flex position-relative" action="${pageContext.request.contextPath}/search"
                      method="get">
                    <div class="position-relative flex-grow-1 me-2">
                        <input id="searchInput" class="form-control form-control-lg border-success w-100"
                               type="search" name="query" placeholder="Tìm kiếm tên sản phẩm, loại trái cây..."
                               aria-label="Search" value="${param.query}" autocomplete="off">

                        <div id="searchSuggestions">
                        </div>
                    </div>

                    <button class="btn btn-primary-custom btn-lg fw-bold" type="submit">
                        <i class="bi bi-search"></i>
                    </button>
                </form>
            </div>
        </div>
    </div>
</section>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const searchInput = document.getElementById('searchInput');
        const searchSuggestions = document.getElementById('searchSuggestions');

        // Format dropdown using bootstrap classes instead of custom style
        searchSuggestions.className = 'list-group position-absolute w-100 shadow mt-1';
        searchSuggestions.style.zIndex = '1050';
        searchSuggestions.style.maxHeight = '300px';
        searchSuggestions.style.overflowY = 'auto';

        let timeout = null;

        searchInput.addEventListener('input', function () {
            clearTimeout(timeout);
            const query = this.value.trim();

            // Người dùng nhập từ 1 ký tự sẽ gợi ý
            if (query.length < 1) {
                searchSuggestions.style.display = 'none';
                return;
            }

            timeout = setTimeout(() => {
                fetch('${pageContext.request.contextPath}/search-suggest?query=' + encodeURIComponent(query))
                    .then(response => response.json())
                    .then(data => {
                        searchSuggestions.innerHTML = '';

                        if (data.length > 0) {
                            // Gợi ý tìm shop (giống Shopee)
                            const shopItem = document.createElement('a');
                            shopItem.href = '${pageContext.request.contextPath}/search?query=' + encodeURIComponent(query);
                            shopItem.className = 'list-group-item list-group-item-action border-0 py-2 px-3 text-dark';
                            shopItem.innerHTML = `<i class="bi bi-shop text-danger me-2"></i>Tìm Shop "` + query + `"`;
                            searchSuggestions.appendChild(shopItem);

                            // Danh sách sản phẩm (chữ trơn)
                            data.forEach(product => {
                                const item = document.createElement('a');
                                item.href = '${pageContext.request.contextPath}/product-detail?id=' + product.id;
                                item.className = 'list-group-item list-group-item-action border-0 py-2 px-3 text-dark';
                                item.innerText = product.name;
                                searchSuggestions.appendChild(item);
                            });
                            searchSuggestions.style.display = 'block';
                        } else {
                            const emptyMsg = document.createElement('div');
                            emptyMsg.className = 'list-group-item border-0 py-2 px-3 text-muted';
                            emptyMsg.innerText = 'Không tìm thấy sản phẩm phù hợp';
                            searchSuggestions.appendChild(emptyMsg);
                            searchSuggestions.style.display = 'block';
                        }
                    })
                    .catch(error => {
                        console.error('Lỗi khi tải gợi ý:', error);
                    });
            }, 300);
        });

        document.addEventListener('click', function (e) {
            if (!searchInput.contains(e.target) && !searchSuggestions.contains(e.target)) {
                searchSuggestions.style.display = 'none';
            }
        });

        searchInput.addEventListener('focus', function () {
            if (this.value.trim().length >= 1 && searchSuggestions.children.length > 0) {
                searchSuggestions.style.display = 'block';
            }
        });
    });
</script>