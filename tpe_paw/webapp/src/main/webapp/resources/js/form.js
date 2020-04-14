function updateForm(element) {
    element.form.submit()
}

// $("#fav-button").on('change', function() {
//     if ($(this).is(':checked')) {
//         $(this).attr('value', 'true');
//     } else {
//         $(this).attr('value', 'false');
//     }
// })

function updateFavValue(favButton) {
    if (favButton.checked) {
        favButton.value = true;
    } else {
        favButton.value = false;
    }
}