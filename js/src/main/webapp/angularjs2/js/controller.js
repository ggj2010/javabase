// 定义一个模块, 并引入依赖的angular模块
var umService = angular.module( 'UserManage', [ 'ngRoute' ] );

function umRouteConfig ( $routeProvider ) {
	// console.log( $routeProvider );
	$routeProvider
	.when( '/', {
		controller: ListController,
		templateUrl: 'list.html'
	})
	.when( '/update/:id/:age', {
		controller: UpdateController,
		templateUrl: 'detail.html'
	})
	.when( '/delete', {
		
	})
	.otherwise({
      redirectTo: '/'
    });
}

umService.config( umRouteConfig );

function ListController ( $scope, $http ) {
	$http.get( 'server/user.json' ).success( function ( data, status, headers, config ) {
		console.log( data );
		$scope.users = data;
	});
}

function UpdateController ( $scope, $http, $routeParams ) {
	var id = $routeParams.id;
	// var age = $routeParams.age;
	// console.log( id );
	$http.get( 'server/user.json' ).success( function ( data, status, headers, config ) {
		// console.log( data[ id ] );
		$scope.xiuUser = getObjById( id, data );	
	});
	
	$scope.update = function () {
		// console.log( $scope.xiuUser )
		$http.get( 'server/user.json', { params: $scope.xiuUser } );
	}
}

function getObjById ( id, obj ) {
	var len = obj.length;
	for(var i=0; i<len; i++){
		if( id == obj[i].id ){
			return obj[i];
		}		
	}
	return null;
}
