function openNav() {
    let elem = document.getElementById("sidenav");
    let menuIcon = document.getElementById("menu-icon");
    if (elem.classList.contains("open")){
        elem.classList.remove("open")
        menuIcon.innerText = "menu"
    } else {
        elem.classList.add("open")
        menuIcon.innerText = "close"
    }
}