
/* Resizes the height of a single card */
function addFadeOutCard(card){
    /*
     * CODE/DESCRIPTION CONTAINER --> If the code is too long, will want it to fade out in the card
     */
    codeBlock = card.querySelector('.card-snippet-content').querySelector('.snippet-code-container').querySelector('.card-snippet-block');
    descrBlock = card.querySelector('.card-snippet-content').querySelector('.card-snippet-block');

    addFadeOutTo(codeBlock, '.card-snippet-fade-out-code', '#DCDCDC');
    addFadeOutTo(descrBlock, '.card-snippet-fade-out-descr', '#FFFFFF');
}

function addFadeOutTo(block, cssClass, color) {
    maxBlockHeight = 200;
    if (block.getBoundingClientRect().height >= (maxBlockHeight - 2)) {
        block.querySelector(cssClass).style.backgroundImage = 'linear-gradient(to bottom, transparent, ' + color +')';
        block.querySelector(cssClass).style.backgroundImage = '-webkit-linear-gradient(to bottom, transparent, ' + color +')';
        block.querySelector(cssClass).style.backgroundImage = '-moz-linear-gradient(to bottom, transparent, ' + color +')';
    }
}

/* Get all the different cards and for each one, resize it to the correct height */
function addFadeOutCards(){
    cardContainer = document.getElementsByClassName('card-item');
    for(i = 0; i < cardContainer.length; i++){
        addFadeOutCard(cardContainer[i]);
    }
}

/* Once the page has loaded, resize all the cards */
window.onload = addFadeOutCards();
