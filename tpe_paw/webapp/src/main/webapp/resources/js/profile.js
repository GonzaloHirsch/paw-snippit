function preview(input) {
    let output = document.getElementById('profile-image');
    output.src = URL.createObjectURL(input.files[0]);
    output.onload = function() {
        URL.revokeObjectURL(output.src)
    }
    let save_btn = document.getElementById('image-confirm');
    let discard_btn = document.getElementById('image-discard');
    save_btn.classList.add("visible");
    discard_btn.classList.add("visible");
    save_btn.classList.remove("hidden-button");
    discard_btn.classList.remove("hidden-button");
}

function hiddenClick(element){
    let input = document.getElementById('profile-image-input');
    input.click();
}

function submitImageForm(element){
    let frm = document.getElementById('image-form-submit');
    frm.click();
}

function submitDescriptionForm (element) {
    let descriptionForm = document.getElementById('description-form');
    descriptionForm.submit();
}