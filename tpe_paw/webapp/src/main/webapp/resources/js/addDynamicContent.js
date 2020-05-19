
function addAdminRow(divId, placeholder) {
    const div = document.createElement("div");

    div.classList.add('flex-row');
    div.classList.add('form-field-container');

    if (divId === "form-dynamic-lang") {
        let langCount = getCounter('langCount');

        div.innerHTML = `
            <input type="text" class="form-border form-field-size form-added-field form-border-style" name="languages[${langCount}]" placeholder="${placeholder}">
            <label>
                <i class="material-icons form-delete-icon">delete</i>
                <input type="button" class="form-remove-button form-border form-button-basics" onclick="removeRow(this, 'form-dynamic-lang', 'langCount', 'langButton')" />
            </label>
          `;
        document.getElementById('langCount').value = ++langCount;

    } else {
        let tagCount = getCounter('tagCount');

        div.innerHTML = `
            <input type="text" class="form-border form-field-size form-added-field form-border-style" name="tags[${tagCount}]" placeholder="${placeholder}">
            <label>
                <i class="material-icons form-delete-icon">delete</i>
                <input type="button" class="form-remove-button form-border form-button-basics" onclick="removeRow(this, 'form-dynamic-tag', 'tagCount', 'tagButton')" />
            </label>
          `;
        document.getElementById('tagCount').value = ++tagCount;
    }

    document.getElementById(divId).appendChild(div);
}

function removeRow(input, divId, counterId, buttonId) {
    document.getElementById(divId).removeChild(input.parentNode.parentElement);
    let counter = getCounter(counterId);
    document.getElementById(counterId).value = --counter;
}

function getCounter(counterId) {
    let counter = parseInt(document.getElementById(counterId).value, 10);
    return isNaN(counter) ? 0 : counter;
}
