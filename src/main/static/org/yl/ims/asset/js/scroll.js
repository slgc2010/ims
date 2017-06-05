
/**
 * 滚动条置顶
 */
$(function() {
	$.fn.manhuatoTop = function(options) {
		var defaults = {			
			showHeight : 150,
			speed : 1000
		};
		var options = $.extend(defaults,options);
		$("body").prepend("<div id='totop'><a></a></div>");
		var $toTop = $(this);
		var $top = $("#totop");
		var $ta = $("#totop a");
		$toTop.scroll(function(){
			var scrolltop=$(this).scrollTop();		
			if(scrolltop>=options.showHeight){				
				$top.show();
			}
			else{
				$top.hide();
			}
		});	
		$ta.hover(function(){ 		
			$(this).addClass("cur");	
		},function(){			
			$(this).removeClass("cur");		
		});	
		$top.click(function(){
			$("html,body").animate({scrollTop: 0}, options.speed);	
		});
	};
	
	initSystemScroll();
	
	
	$.fn.navigationFn = function(){
		var defaults = {
				
		}
		var options = $.extend(defaults,options);
		var tpl = ['<ul class="nav imsnav">',
		           '<li class="active"><a href="/guest#/blog">那些年(日志)</a></li>',
		           '<li class="active"><a href="/say#/say">那些年(心情)</a></li>',
		           '<li class="active" title="百度音乐各种类型排行榜"><a href="http://fm.baidu.com/">音乐榜(TOP)</a></li>',
		           '<li class="active"><a href="/guest#/say">我是机器人</a></li>',
		           '<li class="active"><a href="/user#/blog">关于我</a></li>',
		           '</ul>'].join(" ");
		$("body").prepend("<div id='imsNavigation'>"+tpl+"<img class='img-circle' alt='约吗' src='/asset/images/nav.png'/></div>");
		var $navigation = $("#imsNavigation");
		var $imgNavigation = $("#imsNavigation img");
		var imsNavUl = $("#imsNavigation ul");
		var imsNavli = $("#imsNavigation ul li");
		
		$imgNavigation.click(function(){
			imsNavUl.toggle(700);
		});
		imsNavli.click(function(){
			imsNavUl.toggle(700);
		});
	}
	
	initSystemImsNavigation();
}); 
/**
 * 初始化系统滑动时右边置顶按钮
 */
var initSystemScroll = function(){
	$(window).manhuatoTop({
		showHeight : 100,//设置滚动高度时显示
		speed : 500 //返回顶部的速度以毫秒为单位
	});
};

var initSystemImsNavigation = function(){
	$(window).navigationFn();
}