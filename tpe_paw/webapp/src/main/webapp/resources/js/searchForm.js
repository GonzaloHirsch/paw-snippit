function submitForm(element) {
    let elem = document.getElementById("search-bar")
    let txt = elem.getAttribute("value")
    if (txt !== ""){
        element.form.submit()
    }
}
