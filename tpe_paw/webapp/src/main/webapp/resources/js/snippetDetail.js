function copyContent() {
    let textToCopy = document.getElementById("code-block")
    let textBox = document.getElementById("hidden-code-input")
    textBox.setAttribute('value', textToCopy.innerHTML);
    textBox.select();
    document.execCommand('copy');
}