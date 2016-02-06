var metaslider_30 = function($) {
	$('#metaslider_30').flexslider({
		slideshowSpeed : 6000,
		animation : "fade",
		controlNav : 1,
		directionNav : true,
		pauseOnHover : true,
		direction : "horizontal",
		reverse : false,
		animationSpeed : 600,
		prevText : "&lt;",
		nextText : "&gt;",
		slideshow : true
	});
};
var timer_metaslider_30 = function() {
	var slider = !window.jQuery ? window.setTimeout(timer_metaslider_30, 100)
			: !jQuery.isReady ? window.setTimeout(timer_metaslider_30, 1)
					: metaslider_30(window.jQuery);
};
timer_metaslider_30();