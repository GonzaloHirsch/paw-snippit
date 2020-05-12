
/* Resizes the height of a single card */
function resizeCard(card){
    /*
     * CODE/DESCRIPTION CONTAINER --> If the code is too long, will want it to fade out in the card
     */
    let codeBlock = card.querySelector('.card-snippet-content').querySelector('.snippet-code-container').querySelector('.card-snippet-block');
    let descrBlock = card.querySelector('.card-snippet-content').querySelector('.card-snippet-block');

    addFadeOutTo(codeBlock, '.card-snippet-fade-out-code', '#DCDCDC');
    addFadeOutTo(descrBlock, '.card-snippet-fade-out-descr', '#FFFFFF');

    let heightToUse = card.querySelector('.card-snippet-content').getBoundingClientRect().height;
    console.log(heightToUse);

    if (heightToUse < 100){
        let codeHeight = codeBlock.querySelector(".code-element").getBoundingClientRect().height;
        let descHeight = descrBlock.querySelector(".snippet-text").getBoundingClientRect().height;
        let titleHeight = card.querySelector('.card-snippet-content').querySelector('.snippet-title-container').querySelector(".snippet-title").getBoundingClientRect().height;
        heightToUse = codeHeight > 300 ? 300 : codeHeight;
        heightToUse = heightToUse + (descHeight > 350 ? 350 : descHeight + 50);
        heightToUse = heightToUse + titleHeight + 100;
    }

    console.log(heightToUse);

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

function addFadeOutTo(block, cssClass, color) {
    let maxBlockHeight = 200;
    if (block.getBoundingClientRect().height >= (maxBlockHeight - 2)) {
        block.querySelector(cssClass).style.backgroundImage = 'linear-gradient(to bottom, transparent, ' + color +')';
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
