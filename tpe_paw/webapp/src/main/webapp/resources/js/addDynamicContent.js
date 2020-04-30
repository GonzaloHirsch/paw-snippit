
function addAdminRow(divId) {
    const div = document.createElement("div");

    div.classList.add('flex-row');
    div.classList.add('form-field-container');

    if (divId === "form-dynamic-lang") {
        let langCount = getCounter('langCount');

        div.innerHTML = `
            <input type="text" class="form-border form-field-size form-added-field-padding" name="languages[${langCount}]" placeholder='Language'>
            <input type="button" class="form-remove-button form-border" value="-" onclick="removeRow(this, 'form-dynamic-lang', 'langCount', 'langButton')" />
          `;
        document.getElementById('langCount').value = ++langCount;
        limitFields(langCount, 'langButton');

    } else {
        let tagCount = getCounter('tagCount');

        div.innerHTML = `
            <input type="text" class="form-border form-field-size form-added-field-padding" name="tags[${tagCount}]" placeholder='Tag'>
            <input type="button" class="form-remove-button form-border" value="-" onclick="removeRow(this, 'form-dynamic-tag', 'tagCount', 'tagButton')" />
          `;
        document.getElementById('tagCount').value = ++tagCount;
        limitFields(tagCount, 'tagButton');
    }

    document.getElementById(divId).appendChild(div);
}

function removeRow(input, divId, counterId, buttonId) {
    document.getElementById(divId).removeChild(input.parentNode);
    let counter = getCounter(counterId);
    document.getElementById(counterId).value = --counter;
    limitFields(counter, buttonId);
}

function getCounter(counterId) {
    let counter = parseInt(document.getElementById(counterId).value, 10);
    return isNaN(counter) ? 0 : counter;
}

function limitFields(currentAmount, buttonId) {
    let limit = 5;
    let button = document.getElementById(buttonId);

    if (currentAmount >= limit) {
        button.classList.add("hidden");
    } else {
        button.classList.remove("hidden");
    }
}
