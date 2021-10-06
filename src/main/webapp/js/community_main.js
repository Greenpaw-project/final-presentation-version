/*
*
* 커뮤니티 메인용 js 
*
*/

const swiper = new Swiper('.swiper', {
	// Optional parameters
	//direction: 'horizontal',
	//loop: true,
	
	spaceBetween: 30,
	centeredSlides: true,
	autoplay:{
		delay: 2500,
		disableOnInteraction: false,	
	},

	// If we need pagination
	pagination: {
		el: '.swiper-pagination',
		clickable: true,
	},

	// Navigation arrows
	navigation: {
		nextEl: '.swiper-button-next',
		prevEl: '.swiper-button-prev',
	},
});