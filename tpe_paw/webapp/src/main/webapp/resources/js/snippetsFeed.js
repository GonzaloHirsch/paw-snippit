
/* Resizes the height of a single card */
function resizeCard(card){
    /*
     * CODE/DESCRIPTION CONTAINER --> If the code is too long, will want it to fade out in the card
     */
    codeBlock = card.querySelector('.card-snippet-content').querySelector('.snippet-code-container').querySelector('.card-snippet-block');
    descrBlock = card.querySelector('.card-snippet-content').querySelector('.card-snippet-block');

    addFadeOutTo(codeBlock, '.card-snippet-fade-out-code', '#DCDCDC');
    addFadeOutTo(descrBlock, '.card-snippet-fade-out-descr', '#FFFFFF');

    /*
     * CARD CONTAINER --> Making the card height match the content
     */
    feedGrid = document.getElementsByClassName("feed-snippets-grid")[0];

    rowHeight = parseInt(window.getComputedStyle(feedGrid).getPropertyValue('grid-auto-rows'));
    rowGap = parseInt(window.getComputedStyle(feedGrid).getPropertyValue('grid-row-gap'));

    /* Use the height of the content div to calculate the new height */
    rowSpan = Math.ceil((card.querySelector('.card-snippet-content').getBoundingClientRect().height + rowGap) / (rowHeight + rowGap));

    /* Add the new row span to the card style */
    card.style.gridRowEnd = 'span ' + rowSpan;
}

function addFadeOutTo(block, cssClass, color) {
    maxBlockHeight = 200;
    if (block.getBoundingClientRect().height >= (maxBlockHeight - 2)) {
        block.querySelector(cssClass).style.backgroundImage = 'linear-gradient(to bottom, transparent, ' + color +')';
    }
}

/* Get all the different cards and for each one, resize it to the correct height */
function resizeAllCards(){
    cardContainer = document.getElementsByClassName('card-snippet-container');
    for(i = 0; i < cardContainer.length; i++){
        resizeCard(cardContainer[i]);
    }
}

/* Once the page has loaded, resize all the cards */
window.onload = resizeAllCards();

/* Resize the cards again when the browser is resized */
window.addEventListener('resize', resizeAllCards);
