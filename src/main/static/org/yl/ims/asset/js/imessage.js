/**
 * @author suyuanlin
 */
(function() {
	'use strict';
	var imessage = angular.module('imessage',[ 'config','ui.router'], function($httpProvider) {
		  $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
		  var param = function(obj) {
		    var query = '', name, value, fullSubName, subName, subValue, innerObj, i;
		    for(name in obj) {
		      value = obj[name];
		      if(value instanceof Array) {
		        for(i=0; i<value.length; ++i) {
		          subValue = value[i];
		          fullSubName = name + '[' + i + ']';
		          innerObj = {};
		          innerObj[fullSubName] = subValue;
		          query += param(innerObj) + '&';
		        }
		      }
		      else if(value instanceof Object) {
		        for(subName in value) {
		          subValue = value[subName];
		          fullSubName = name + '[' + subName + ']';
		          innerObj = {};
		          innerObj[fullSubName] = subValue;
		          query += param(innerObj) + '&';
		        }
		      }
		      else if(value !== undefined && value !== null)
		        query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
		    }
		      
		    return query.length ? query.substr(0, query.length - 1) : query;
		  };
		 
		  // Override $http service's default transformRequest
		  $httpProvider.defaults.transformRequest = [function(data) {
		    return angular.isObject(data) && String(data) !== '[object File]' ? param(data) : data;
		  }];
	});
	imessage.run(['$rootScope','cfg',function($rootScope,cfg) {
		angular.extend($rootScope, cfg);
	}]);
	
	var isFn = angular.isFunction;
	var forEach = angular.forEach;
	function toJson(j) {
		return (new Function("return " + j))();
	}
	function getTime(){
		var data = new Date();
		return data={
				currentTime:Date().getTime()
		};
	}
	var asset, root, common;
	configVar.$inject = [ 'cfg' ];
	function configVar(cfg) {
		asset = cfg.root+'/asset/';
		root = cfg.ims;
		common = asset + 'tpl/';
	}
	imessage.config(configVar);
	
	configUIView.$inject = [ '$stateProvider', '$urlRouterProvider', 'cfg' ];
	function configUIView($stateProvider, $urlRouterProvider, cfg) {
		var root = $stateProvider;
		if (cfg.me == cfg.uid) {
			$urlRouterProvider.otherwise("/blog");
			root.state('index', {
				url : '/index',
				templateUrl : common + 'blog.html'
			});
		} else {
			$urlRouterProvider.otherwise("/blog");
		}
		root.state('blog', {
			url : '/blog',
			templateUrl : common + 'blog.html'
		});
		root.state('user',{
			url:'/user',
			templateUrl : common + 'user.html'
		});
		root.state('managerArticle',{
			url:'/managerArticle',
			templateUrl : common + 'blog-list.html'
		});
		root.state('badd',{
			url:'/badd',
			templateUrl : common + 'add-blog.html'
		});
		root.state('eblog',{
			url:'/eblog',
			templateUrl : common + 'add-blog.html'
		});
		root.state('say',{
			url:'/say',
			templateUrl : common + 'say.html'
		});
		root.state('publishsay',{
			url:'/publishsay',
			templateUrl : common + 'shuo.html'
		});
		root.state('admin',{
			url:'/admin',
			templateUrl : common + 'admin.html'
		});
		root.state('message',{
			url:'/message',
			templateUrl : common + 'message.html'
		});
		root.state('upload',{
			url:'/upload',
			templateUrl : common + 'upload.html'
		});
		root.state('file',{
			url:'/file',
			templateUrl : common + 'filelist.html'
		});
	}
	imessage.config(configUIView);

	buildViews.$inject = [ '$rootScope', '$stateParams', '$http' ];
	function buildViews($rootScope, $stateParams, $http) {
		var views = {};
		$rootScope.views = views;
		views.getFooter = function() {
			return common + 'footer.html';
		};
	}
	imessage.run(buildViews);
	
	scollService.$inject=[];
	function scollService(){
		var $window = $(window);
		return {
			//
			scrollHeight: function(){
				return $window.height() + $window.scrollTop();
			},
			// when scoll height above the selector height, the callback will be
			// trigger
			bindSelector: function(selector, callback){
				var self = this;
				$window.on('scroll', function(evt){
					var o = $(selector).offset();
					if(o && o.top < self.scrollHeight()){
						callback();
					}
				});
			},
			// when scoll is change, the callback will be trigger
			bindTop: function(callback){
				$window.on('scroll', function(evt){
					callback(evt);
				});
			}
		};
	}
	imessage.factory('scollService',scollService);
	
	modalService.$inject=['$q','$rootScope','$compile','$sce'];
	function modalService($q,$rootScope,$compile,$sce) {
		var modalTemplate;
		$.get(asset+'tpl/modal-tpl.html').done(function(r){
			modalTemplate=r;
		});
		var succ = 'text-success';
		var warn = 'text-warning';
		var error = 'text-danger';
		this.confirm=function(param,option){
			var q=$q.defer();
			var p={title:'提示',titleCls:succ,content:'',result:'',size:'md',isConfirm:true};
			if(angular.isObject(param)){
				angular.extend(p,param);
			}
			var $scope=$rootScope.$new();
			angular.extend($scope,p);
			console.log($scope);
			$scope.confirm=function(){
				q.resolve($scope.result);
			};
			$scope.cancel=function(){
				q.reject();
			};
			var element=$compile(modalTemplate)($scope);
			$scope.$$phase||$scope.$apply();
			element.unbind('hidden.bs.modal');
			element.on('hidden.bs.modal',function(){
				q.reject();
				setTimeout(function(){$scope.$destroy();element.remove();});
			});
			
			element.unbind('shown.bs.modal');
			element.on('shown.bs.modal',function(){
				q.notify(element);
			})
			option=option||'show';
			element.modal(option);
			if(p.duration){
				setTimeout(function(){
//					element.modal('hide');
				},p.duration);
			}
			return q.promise;
		};
		this.show=function(param,option){
			var p={title:'提示',titleCls:succ,content:'',size:'md',isConfirm:false};
			angular.extend(p,param);
			return this.confirm(p,option);
		}
		this.warn = function(content,duration) {
			return this.show({content:content,titleCls:warn,size:'sm',duration:duration})
		}
		this.succ = function(content,duration) {
			return this.show({content:content,titleCls:succ,size:'sm',duration:duration})
		}
		this.error = function(content,duration) {
			return this.show({content:content,titleCls:error,size:'sm',duration:duration})
		}
		return this;
	}
	imessage.factory('modalService', modalService);
	
	imessage.filter('to_trusted', ['$sce', function ($sce) {
		return function (text) {
		    return $sce.trustAsHtml(text);
		};
	}]);
	
	imgDirective.$inject=['$http'];
	function imgDirective($http){
		return {
			restrict : 'A',
			link: function(scope,element,attr){
				function loadData(){
					$http.post('/bingapi/getBingData',{previous:0}).success(function(r){
						element.css("background-image","url("+r.imgUrl+")");
						element.css("background-attachment","fixed");
						element.css("background-center","center");
					});
				}
				loadData();
			}
		};
	}
	imessage.directive('imgBing',imgDirective);
	
	showMsgDirective.$injct=['$http'];
	//<message string-html>${messageInfo.messageinfo }</message>
	function showMsgDirective($http){
		return {
			restrict : 'EA',
			replace: true,
			template:'<span class="message" ng-bind-html="message | to_trusted"></span>',
			link:function(scope,element,attr){
				function loadData(){
					$http.post('/message/getFirstMessage',{status:1}).success(function(r){
						scope.message =r.messageInfo;
						console.log(r);
					});
				}
				loadData();
			}
		};
	}
	imessage.directive('showMsg',showMsgDirective);
	
	imsDownload.$inject=['$http'];
	function imsDownload($http){
		return {
			restrict : 'A',
			link: function(scope,element,attr){
				function get(){
					var rid=attr.rid||scope.item.id||'';
					return {fileId:rid};
				}
				function download(){
					$http.post('/fileapi/download',get()).success(function(r){
						console.log(r)
						//element.append("<iframe style=\"display:none\" width=0 height=0 src='"+r+"' ></iframe>");
					}).error(function(e){
						
					});
				}
				element.bind('click',download);
			}
		};
	}
	imessage.directive('imsDownload',imsDownload);
	
    function format(time,sf){
	    var t = new Date(time);
	    var tf = function(i){return (i < 10 ? '0' : '') + i}; 
	    return sf.replace(/yyyy|MM|dd|HH|mm|ss/g,function(f){
	        switch(a){
	            case 'yyyy': 
	                return tf(t.getFullYear()); 
                case 'MM': 
	                return tf(t.getMonth() + 1); 
                case 'mm':
                    return tf(t.getMinutes()); 
                case 'dd':
                    return tf(t.getDate()); 
                case 'HH':
                    return tf(t.getHours()); 
                case 'ss':
                    return tf(t.getSeconds()); 
	        }
	    });
    }
	
})();
