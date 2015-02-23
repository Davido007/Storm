var nav = $('#nav');
var selection = $('.dropdown-menu');
var select = selection.find('li');

nav.click(function(event) {
    if (nav.hasClass('active')) {
        nav.removeClass('active');
        selection.stop().slideUp(200);
    } else {
        nav.addClass('active');
        selection.stop().slideDown(200);
    }
    event.preventDefault();
});

select.click(function(event) {
	alert('sds');
});