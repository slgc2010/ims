/**
 * @author suyuanlin
 */
(function() {
	var imessage = angular.module('imessage');
	var pageSize = 10;
	var origin = location.origin || "";
	var pathName = window.document.location.pathname;
	var projectName = pathName
			.substring(0, pathName.substr(1).indexOf('/') + 1);

	imessageContextController.$inject = [ '$scope', '$http', 'scollService' ];
	function imessageContextController($scope, $http, scollService) {
		$scope.pageIndex = 0;
		$scope.total = '';
		$scope.articles = [];

		$scope.doGetMore = function() {
			isLoad = false;
			$scope.pageIndex++;
			var localUrl = origin + '/blog/bloglist';
			$.post(localUrl, {
				pageIndex : $scope.pageIndex,
				pageSize : pageSize || 10
			}).success(function(r) {
				$scope.isShow = false;
				var articles = r.list;
				for (var i = 0; i < articles.length; i++) {
					var item = articles[i];
					if (item.imageUrl == null) {
						item.classes = "classes";
					}
					$scope.articles.push(item);
				}
				$scope.$apply();
				isLoad = true;

				if ($scope.articles.length >= r.totalCount) {
					isAll = true;
				}
			}).error(function(r) {
				$scope.isShow = false;
				isLoad = false;
				$scope.$apply();
			});
		};
		$scope.isShow = true;

		$scope.getTime = function() {
			return new Date().getTime();
		};

		var isLoad = true;
		var isAll = false;

		scollService.bindSelector('.feed:last', function() {
			if (isLoad && !isAll) {
				$scope.doGetMore();
			}
		});
	}
	imessage.controller('imessageContextController', imessageContextController);

	userInfoController.$inject = [ '$scope', '$http' ];
	function userInfoController($scope, $http) {
		$scope.users = [];
		$scope.pageIndex = 0;
		$scope.total = '';

		$scope.doGetMore = function() {
			$scope.pageIndex++;
			var localUrl = ':ims:/user/getUserInfoData';
			$http.post(localUrl, {
				pageIndex : $scope.pageIndex,
				pageSize : pageSize || 10
			}).success(function(r) {
				var users = r.users.list;
				$scope.totalPage = r.users.totalPage;
				$scope.users = [];
				for (var i = 0; i < users.length; i++) {
					var item = users[i];
					$scope.users.push(item);
				}
				// $scope.$apply();
			});
		};

		$scope.prev = function() {
			if ($scope.pageIndex > 1) {
				$scope.pageIndex = $scope.pageIndex - 2;
				$scope.doGetMore();
			}
		};

		$scope.next = function() {
			if ($scope.pageIndex < $scope.totalPage) {
				$scope.doGetMore();
			}
		};
	}
	imessage.controller('userInfoController', userInfoController);

	blogManagerController.$inject = [ '$scope', '$http' ];
	function blogManagerController($scope, $http) {
		$scope.pageIndex = 0;
		$scope.total = '';
		$scope.articles = [];
		$scope.doGetMore = function() {
			$scope.pageIndex++;
			var localUrl = origin + '/blog/getArtilceData';
			$http.post(localUrl, {
				pageIndex : $scope.pageIndex,
				pageSize : pageSize || 10
			}).success(function(r) {
				var articles = r.articles.list;
				$scope.articles = [];
				$scope.totalPage = r.articles.totalPage;
				for (var i = 0; i < articles.length; i++) {
					var item = articles[i];
					$scope.articles.push(item);
				}
				// $scope.$apply();
			});
		};

		$scope.prev = function() {
			if ($scope.pageIndex > 1) {
				$scope.pageIndex = $scope.pageIndex - 2;
				$scope.doGetMore();
			}
		};

		$scope.next = function() {
			if ($scope.pageIndex < $scope.totalPage) {
				$scope.doGetMore();
			}
		};
	}
	imessage.controller('blogManagerController', blogManagerController);

	createBlogController.inject = [ '$scope', '$http' ];
	function createBlogController($scope, $http) {

	}
	imessage.controller('createBlog', createBlogController);
    
    var format = function (time,sf){
	    var t = new Date(time);
	    var tf = function(i){return (i < 10 ? '0' : '') + i}; 
	    return sf.replace(/yyyy|MM|dd|HH|mm|ss/g,function(f){
	        switch(f){
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
	sayController.$inject = [ '$scope', '$http', 'scollService' ];
	function sayController($scope, $http, scollService) {
		var date = new Date();
		var currentYear = date.getFullYear();
		$scope.cYear = date.getFullYear();
		$scope.pageNo = 0;
		$scope.total = 0;
		$scope.shuoshuo = [];
		$scope.cYear = '2015';
		$scope.doGetMore = function() {
			isLoad = false;
			$scope.pageNo++;
			$http.post(origin + '/memory/getShuoShuo/', {
				pageNumber : $scope.pageNo,
				pageSize : 365 || pageSize
			}).success(function(r) {
				var shuoshuoes = r.list;
				debugger
				for (var i = 0; i < shuoshuoes.length; i++) {
					var item = shuoshuoes[i];
					var sayYear = format(item.createTime,'yyyy');
					if (i == 0 || sayYear != currentYear) {
						item.year = sayYear || currentYear;
						currentYear = sayYear;
					}
					$scope.shuoshuo.push(item);
				}
				$scope.isShow = false;
				isLoad = true;
				if ($scope.shuoshuo.length >= r.totalCount) {
					isAll = true;
				}
			});
		};
		$scope.isShow = true;
		var isLoad = true;
		var isAll = false;
		scollService.bindSelector('.feed:last', function() {
			if (isLoad && !isAll) {
				$scope.doGetMore();
			}
		});
	}
	imessage.controller('sayController', sayController);

	imessageCountController.$inject = [ '$scope', '$http' ];
	function imessageCountController($scope, $http) {
		$scope.sayCount = 0;
		$scope.blogCount = 0;
		$scope.msgCount = 0;
		$scope.previewCount = visits;
		$scope.dataSize = 0;
		$scope.osDataInfo, $scope.props;

		$http.post(origin + '/message/getBlogCount').success(function(r) {
			$scope.blogCount = r.blogCount;
		});
		$http.post(origin + '/message/getSayCount').success(function(r) {
			$scope.sayCount = r.sayCount;
		});
		$http.post(origin + '/message/getMsgCount').success(function(r) {
			$scope.msgCount = r.msgCount;
		});
		// $http.post(origin + '/message/getPreviewCount').success(function(r){
		// $scope.previewCount = r.previewCount;
		// });
		$http.post(origin + '/message/getUseDataBaseSize/').success(
				function(r) {
					$scope.dataSize = r.osDataInfo.totalMemory;
					$scope.osDataInfo = r.osDataInfo;
					$scope.props = r.osDataInfo.props;
				});
	}
	imessage.controller('imessageCountController', imessageCountController);

	msgManagerController.$inject = [ '$scope', '$http' ];
	function msgManagerController($scope, $http) {
		$scope.pageIndex = 0;
		$scope.messages = [];
		$scope.doGetMore = function() {
			$scope.pageIndex++;
			$http.post(origin + '/message/getMessageInfo', {
				pageNumber : $scope.pageIndex,
				pageSize : pageSize || 10
			}).success(function(data) {
				var ms = data.messages.list;
				for (var i = 0; i < ms.length; i++) {
					var item = ms[i];
					$scope.messages.push(item);
				}
			});
		};

		$scope.isOpen = function(data) {
			return data.status == 1;
		};

		$scope.isClose = function(data) {
			return data.status == 0;
		};

		$scope.isShow = function(data) {
			var localUrl = origin + '/message/updateMessageInfo';
			$http.post(localUrl, {
				messageId : data.id,
				messageStatus : data.status
			}).success(function(r) {
				if (data.status == 1) {
					data.status = 0;
				} else if (data.status == 0) {
					var m = $scope.messages;
					for (var i = 0; i < m.length; i++) {
						m.status = 0;
					}
					data.status = 1;
				}
			});
		};

	}
	imessage.controller('msgManagerController', msgManagerController);

	fileController.$inject = [ '$scope', '$http','modalService'];
	function fileController($scope, $http,modalService) {
		$scope.upload = function(){
			$.ajaxFileUpload({
		        url:'fileapi/upload',//处理图片脚本
		        secureuri :false,
		        fileElementId :'fileToUpload',//file控件id
		        dataType:'text',
		        success : function (data,status){
		        	var idString = (data.substring(1,data.length-1).replace(/\"/g,"")).split(",");
		        	for (var i in idString) {
		        		var id = idString[i];
		        		$("#imgBox").append('<img src="/fileapi/download?fileId='+id+'" width="100">');
		        	}
		        },
		        error: function(e){
		            console.log(e+"!success,You're a programmer, too. ");
		        }
			});
		};
		$scope.articles=[];
		$scope.loadData = function(){
			$http.post('/fileapi/showlist').success(function(r){
				console.log(r);
				$scope.articles=r;
			}).error(function(e){
				console.log(e);
			});
		};
		$scope.preview = function(data){
			console.log(data.id);
			var param={title:data.fileName||'预览',content:'<span>111111111111111111111111</span>'};
			modalService.show(param);
		};
	}
	imessage.controller('fileController', fileController);
})();