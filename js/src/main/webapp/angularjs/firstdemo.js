/**
 * Created by Administrator on 2016/3/16.
 */
    //angular.module（）方法用来声明模块，这个方法接受两个参数，第一个是模块的名称，第二个依赖列表。
//angular.module（'myapp',[]）相当于set方法，用来定义模块的
//    angular.module('myapp')相当于get方法，我们可以在返回的对象上面创建应用了。
var app = angular.module('myApp', []);

app.controller('MyController', function ($scope, $timeout) {
    var updateClock = function () {
        $scope.clock = new Date();
        $timeout(function () {
            updateClock();
        }, 1000);
    };
    updateClock();
});
app.controller('Myclock', function ($scope, $timeout) {
    var updateClock = function () {
        $scope.clock ={
            now: new Date(),
            remark:"今天是20160316"
        }

        $timeout(function () {
            updateClock();
        }, 1000);
    };
    updateClock();
});

app.controller('myCtrl', function ($scope) {
    $scope.firstName = "高";
    $scope.lastName = "广金";
});



app.run(function($rootScope){
    $rootScope.testscope="$rootScope是AngularJS中最接近全局作用域的对象。在$rootScope上附加太多业务逻并不是好主意，这与污染JavaScript的全局作用域是一样的";
});


app.controller("onclickController",function($scope){
    //初始化counter为0
    $scope.counter=0;
    //声明add方法和subtrace
    $scope.add=function(i){
        $scope.counter+=i;
    };
    $scope.substract=function(i){
        $scope.counter-=i;
    };

    $scope.change=function(value){
        alert("选中了:"+value);
    }

});