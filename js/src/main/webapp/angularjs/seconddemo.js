var app = angular.module('secondApp', ['ui.bootstrap.showErrors']);

//先声明父类 再写子类
app.controller('parentController', function ($scope) {
    $scope.person = {greeted: false};
});
app.controller('childController', function ($scope) {
    $scope.sayHello = function () {
        $scope.person.name = 'Ari Lerner';
    };
});

/*
 app.controller('emailController', function ($scope, $interpolate) {
 // 设置监听
 $scope.$watch('emailBody', function (body) {
 if (body) {
 var templates = $interpolate(body);
 $scope.previewText = templates({to: $scope.to});
 }
 });
 });*/
app.controller('emailController', function ($scope, $interpolate) {
    // 设置监听
    $scope.$watch('emailBody', function (body) {
        if (body) {
            var template = $interpolate(body);
            $scope.previewText = template({to: $scope.to});
        }
    });
});

app.controller("filterController", function ($scope, $filter) {
    $scope.param = {
        dates: new Date(),
        name: $filter('lowercase')('GAO'),
        money: $filter('currency')('1999.9999'),
        number: $filter('number')('1999.9999')
    };
    var clockUpdate = function () {
        $scope.param.dates = new Date();
    }
    setInterval(function () {
        $scope.$apply(clockUpdate);
    }, 1000);

})

/*注册*/
app.controller("signupController", function ($scope, $http) {

    $scope.submitForm = function (people, isValid) {
        $scope.$broadcast('show-errors-check-validity');
        //check to make sure the form is completely valid
        if (isValid) {
            $http({
                method: 'POST',
                url: 'test.do',
                data: $.param(people), // pass in data as strings
                headers: {'Content-Type': 'application/x-www-form-urlencoded'} // set the headers so angular passing info as form data (not request payload)
            }).success(function (data) {
                console.log(data);
                if (!data.success) {
                    // if not successful, bind errors to error variables
                    $scope.errorName = data.errors.name;
                    $scope.errorSuperhero = data.errors.superheroAlias;
                } else {
                    // if successful, bind success message to message
                    $scope.message = data.message;
                }
            }).error(function (data, header, config, status) {
                alert(data);
                //处理响应失败
            });
        }
    };

});

app.config(['showErrorsConfigProvider', function (showErrorsConfigProvider) {
    showErrorsConfigProvider.showSuccess(true);
}]);