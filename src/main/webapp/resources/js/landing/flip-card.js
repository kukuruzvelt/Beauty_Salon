$(document).ready(function () {
    $('#stylists .stylists-slider .item .rectangle').click(function () {
        $('#stylists .stylists-slider .item .rectangle').toggleClass('active');
    });

    $('#stylists .stylists-slider .slick-arrow').click(function () {
        $('#stylists .stylists-slider .item .rectangle').removeClass('active');
    });
});