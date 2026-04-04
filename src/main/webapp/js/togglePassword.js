export function togglePassword(){
    const btn = document.querySelectorAll('.btn-toggle-Password');

    btn.forEach(btn =>{
        btn.addEventListener("click", function (){
            const container = this.closest('.input-group');
            const input = container.querySelector('input');
            const icon = this.querySelector('i');

            if(input){
                const type = input.getAttribute('type') === 'password'?'text':'password';
                input.setAttribute('type',type);

                if(icon){
                    icon.classList.toggle('bi-eye');
                    icon.classList.toggle('bi-eye-slash');
                }
            }
        });
    });
}