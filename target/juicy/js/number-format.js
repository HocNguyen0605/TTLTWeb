export function numberFormat() {
    document.querySelectorAll('.price-format').forEach(pf => {
        const value = pf.getAttribute('data-value');
        pf.innerText = new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(value);
    })
}

document.addEventListener("DOMContentLoaded", numberFormat);