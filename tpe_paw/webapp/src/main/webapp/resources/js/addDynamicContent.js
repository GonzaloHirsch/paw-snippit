
function addAdminRow(divId) {
    const div = document.createElement("div");

    div.className = 'row';

    if (divId === "form-dynamic-lang") {
        div.innerHTML = `
             <input type="text" name="languages[0]" placeholder='Language'>
            <input type="button" value="-" onclick="removeRow(this, 'form-dynamic-lang')" />
          `;
    } else {
        div.innerHTML = `
             <input type="text" name="tags[0]" placeholder='Tag'>
            <input type="button" value="-" onclick="removeRow(this, 'form-dynamic-tag')" />
          `;
    }

    document.getElementById(divId).appendChild(div);
}

function removeRow(input, divId) {
    document.getElementById(divId).removeChild(input.parentNode);
}

