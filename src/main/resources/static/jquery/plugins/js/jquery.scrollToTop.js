/*
 * ! jQuery scrollTopTop v1.0 - 2021-11-18 (c) 2013 胡桃夹子
 */
$(function() {
  $.fn.scrollToTop = function(options) {

    $("body").prepend("<a href='#top' id='scroll_totop'></a>");

    var defaults = {
      speed: 200
    };

    $.extend(defaults, {
      speed: options
    });

    return this.each(function() {
      var $toTop = $("#scroll_totop");
      $(window).scroll(function() {
        100 < $(this).scrollTop() ? $toTop.fadeIn() : $toTop.fadeOut();
      });
      $toTop.click(function(event) {
        event.preventDefault();
        $("body, html").animate({
          scrollTop: 0
        }, defaults.speed);
      });
    });


  };
});
