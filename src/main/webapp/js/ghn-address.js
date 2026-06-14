document.addEventListener("DOMContentLoaded", function () {
    const provinceSelect = document.getElementById("provinceSelect");
    const districtSelect = document.getElementById("districtSelect");
    const wardSelect = document.getElementById("wardSelect");

    if (!provinceSelect || !districtSelect || !wardSelect) return;

    // Lấy ID/Code đã lưu (nếu có) từ data attributes
    const savedProvinceId = provinceSelect.getAttribute("data-province-id");
    const savedDistrictId = districtSelect.getAttribute("data-district-id");
    const savedWardCode = wardSelect.getAttribute("data-ward-code");

    // Lấy contextPath để build URL API proxy
    const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
    // Fallback nếu chạy ở root path
    const apiBasePath = contextPath.length > 1 ? contextPath + "/api/ghn" : "/api/ghn";

    // 1. Load Provinces
    fetch(`${apiBasePath}/province`)
        .then(response => response.json())
        .then(data => {
            if (data && data.code === 200 && data.data) {
                let options = '<option value="">-- Chọn Tỉnh/Thành phố --</option>';
                data.data.forEach(province => {
                    options += `<option value="${province.ProvinceID}">${province.ProvinceName}</option>`;
                });
                provinceSelect.innerHTML = options;

                if (savedProvinceId) {
                    provinceSelect.value = savedProvinceId;
                    loadDistricts(savedProvinceId, savedDistrictId);
                }
            }
        })
        .catch(err => console.error("Error loading provinces:", err));

    // 2. Xử lí sự kiện change province
    provinceSelect.addEventListener("change", function () {
        const provinceId = this.value;
        districtSelect.innerHTML = '<option value="">-- Chọn Quận/Huyện --</option>';
        wardSelect.innerHTML = '<option value="">-- Chọn Phường/Xã --</option>';

        if (provinceId) {
            loadDistricts(provinceId, null);
        }
    });

    // 3. Handle District Change
    districtSelect.addEventListener("change", function () {
        const districtId = this.value;
        wardSelect.innerHTML = '<option value="">-- Chọn Phường/Xã --</option>';

        if (districtId) {
            loadWards(districtId, null);
        }
    });

    function loadDistricts(provinceId, selectedDistrictId) {
        fetch(`${apiBasePath}/district`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `province_id=${provinceId}`
        })
            .then(response => response.json())
            .then(data => {
                if (data && data.code === 200 && data.data) {
                    let options = '<option value="">-- Chọn Quận/Huyện --</option>';
                    data.data.forEach(district => {
                        options += `<option value="${district.DistrictID}">${district.DistrictName}</option>`;
                    });
                    districtSelect.innerHTML = options;

                    if (selectedDistrictId) {
                        districtSelect.value = selectedDistrictId;
                        loadWards(selectedDistrictId, savedWardCode);
                    }
                }
            })
            .catch(err => console.error("Error loading districts:", err));
    }

    function loadWards(districtId, selectedWardCode) {
        fetch(`${apiBasePath}/ward`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `district_id=${districtId}`
        })
            .then(response => response.json())
            .then(data => {
                if (data && data.code === 200 && data.data) {
                    let options = '<option value="">-- Chọn Phường/Xã --</option>';
                    data.data.forEach(ward => {
                        options += `<option value="${ward.WardCode}">${ward.WardName}</option>`;
                    });
                    wardSelect.innerHTML = options;

                    if (selectedWardCode) {
                        wardSelect.value = selectedWardCode;
                        updateShippingFee(districtId, selectedWardCode);
                    }
                }
            })
            .catch(err => console.error("Error loading wards:", err));
    }

    wardSelect.addEventListener("change", function () {
        const districtId = districtSelect.value;
        const wardCode = this.value;
        if (districtId && wardCode) {
            updateShippingFee(districtId, wardCode);
        }
    });

    function updateShippingFee(districtId, wardCode) {
        fetch(`${apiBasePath}/fee`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `district_id=${districtId}&ward_code=${wardCode}`
        })
            .then(response => response.json())
            .then(data => {
                if (data && data.code === 200 && data.data && data.data.total != null) {
                    const fee = data.data.total;
                    const shippingFeeElem = document.getElementById("shippingFee");
                    if (shippingFeeElem) {
                        shippingFeeElem.innerHTML = new Intl.NumberFormat('vi-VN').format(fee) + ' đ';
                    }
                    updateTotal(fee);
                }
            })
            .catch(err => console.error("Error calculating fee:", err));
    }

    function updateTotal(newShippingFee) {
        const totalPriceElem = document.getElementById("totalPrice");
        const totalDiscountElem = document.getElementById("totalDiscount");
        const totalElem = document.getElementById("total");

        if (totalPriceElem && totalDiscountElem && totalElem) {
            const parseValue = (text) => parseInt(text.replace(/[^0-9]/g, '')) || 0;
            const totalPrice = parseValue(totalPriceElem.innerText);
            const totalDiscount = parseValue(totalDiscountElem.innerText);
            let total = totalPrice - totalDiscount + newShippingFee;
            if (total < 0) total = 0;

            totalElem.innerHTML = new Intl.NumberFormat('vi-VN').format(total) + ' đ';
            
            // Cập nhật input hidden nếu form order cần (nếu có)
            let hiddenFeeInput = document.getElementById("hiddenShippingFee");
            if (!hiddenFeeInput) {
                hiddenFeeInput = document.createElement("input");
                hiddenFeeInput.type = "hidden";
                hiddenFeeInput.id = "hiddenShippingFee";
                hiddenFeeInput.name = "shippingFee";
                const checkoutForm = document.querySelector("#checkoutModal form");
                if (checkoutForm) checkoutForm.appendChild(hiddenFeeInput);
            }
            if (hiddenFeeInput) hiddenFeeInput.value = newShippingFee;
        }
    }
});
