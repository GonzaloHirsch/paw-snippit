
/* Resizes the height of a single card */
function resizeCard(card){
    /*
     * CODE/DESCRIPTION CONTAINER --> If the code is too long, will want it to fade out in the card
     */
    // Code and description html blocks
    let codeBlock = card.querySelector('.card-snippet-content').querySelector('.snippet-code-container').querySelector('.card-snippet-block');
    let descrBlock = card.querySelector('.card-snippet-content').querySelector('.card-snippet-block');

    // Height of the card
    let heightToUse = card.querySelector('.card-snippet-content').getBoundingClientRect().height;

    // Accounting for Safari not working properly and breaking all grids
    // Height is approximated, giving usually an overestimated size, but no text is cut
    if (heightToUse < 100){
        addFadeOutTo(codeBlock, codeBlock.querySelector(".code-element").querySelector("pre").querySelector("code").getBoundingClientRect().height, '.card-snippet-fade-out-code', '#DCDCDC');
        addFadeOutTo(descrBlock, descrBlock.querySelector(".snippet-text").getBoundingClientRect().height, '.card-snippet-fade-out-descr', '#FFFFFF');
    } else {
        addFadeOutTo(codeBlock, codeBlock.getBoundingClientRect().height, '.card-snippet-fade-out-code', '#DCDCDC');
        addFadeOutTo(descrBlock, descrBlock.getBoundingClientRect().height, '.card-snippet-fade-out-descr', '#FFFFFF');
    }
    // Accounting for Safari not working
    if (heightToUse < 100){
        let codeHeight = codeBlock.querySelector(".code-element").getBoundingClientRect().height;
        let descHeight = descrBlock.querySelector(".snippet-text").getBoundingClientRect().height;
        let titleHeight = card.querySelector('.card-snippet-content').querySelector('.snippet-title-container').querySelector(".snippet-title").getBoundingClientRect().height;
        heightToUse = codeHeight > 200 ? 200 : codeHeight + 25;
        heightToUse = heightToUse + (descHeight > 200 ? 200 : descHeight + 125);
        heightToUse = heightToUse + titleHeight + 100;
    }

    /*
     * CARD CONTAINER --> Making the card height match the content
     */
    let feedGrid = document.getElementsByClassName("feed-snippets-grid")[0];

    let rowHeight = parseInt(window.getComputedStyle(feedGrid).getPropertyValue('grid-auto-rows'));
    let rowGap = parseInt(window.getComputedStyle(feedGrid).getPropertyValue('grid-row-gap'));

    /* Use the height of the content div to calculate the new height */
    let rowSpan = Math.ceil((heightToUse + rowGap) / (rowHeight + rowGap));

    /* Add the new row span to the card style */
    card.style.gridRowEnd = 'span ' + rowSpan;
}

function addFadeOutTo(block, clientH, cssClass, color) {
    let maxBlockHeight = 200;
    let clientRecH = block.getBoundingClientRect().height;
    // Accounts for Safari not working by checking the size of the obtained rectangle

    if (clientRecH < 40){
        if (clientH >= (maxBlockHeight - 2)) {
            block.querySelector(cssClass).classList.remove('hidden');
        }
    } else {
        if (clientRecH >= (maxBlockHeight - 10)) {
            block.querySelector(cssClass).classList.remove('hidden');
        }
    }
}

/* Get all the different cards and for each one, resize it to the correct height */
function resizeAllCards(){
    let cardContainer = document.getElementsByClassName('card-snippet-container');
    for(let i = 0; i < cardContainer.length; i++){
        resizeCard(cardContainer[i]);
    }
}

/* Once the page has loaded, resize all the cards */
window.onload = resizeAllCards();

/* Resize the cards again when the browser is resized */
window.addEventListener('resize', resizeAllCards);


// /* Resizes the height of a single card */
// function addFadeOutCard(card){
//     /*
//      * CODE/DESCRIPTION CONTAINER --> If the code is too long, will want it to fade out in the card
//      */
//     let codeBlock = card.querySelector('.card-snippet-content').querySelector('.snippet-code-container').querySelector('.card-snippet-block');
//     let descrBlock = card.querySelector('.card-snippet-content').querySelector('.card-snippet-block');
//
//     addFadeOutTo(codeBlock, '.card-snippet-fade-out-code', '#DCDCDC');
//     addFadeOutTo(descrBlock, '.card-snippet-fade-out-descr', '#FFFFFF');
// }
//
// function addFadeOutTo(block, cssClass, color) {
//     let maxBlockHeight = 200;
//     if (block.getBoundingClientRect().height >= (maxBlockHeight - 2)) {
//         block.querySelector(cssClass).classList.remove('hidden');
//     }
// }
//
// /* Get all the different cards and for each one, resize it to the correct height */
// function addFadeOutCards(){
//     cardContainer = document.getElementsByClassName('card-item');
//     for(i = 0; i < cardContainer.length; i++){
//         addFadeOutCard(cardContainer[i]);
//     }
// }
//
// /* Once the page has loaded, resize all the cards */
// window.onload = addFadeOutCards();
//

