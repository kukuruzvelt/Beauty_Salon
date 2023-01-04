$(document).ready(function () {
    $('.header-burger, .shadow-background').click(function () {
        $('.header-burger, .menu-list, .authorization, .shadow-background').toggleClass('active');
    });

    $('.menu-list li, .authorization .sign-up').click(function () {
        $('.header-burger, .menu-list, .authorization, .shadow-background').removeClass('active');
    });
});