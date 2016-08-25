//自定义指令
var app = angular.module('threeApp', []);
//驼峰原则 myDirective
app.directive("myDirective", function () {
    return {
        //去除模板标签痕迹
        replace: true,
        //元素（ E）、属性（ A）、类（ C）或注释（ M） 指令的性质
        //推荐使用A，跨浏览器兼容性
        restrict: 'A',
        template: '<a href="http://ggjlovezjy.date">Click me to go to ggj</a>'
      /*  templates: '<a href="{{ myUrl }}">Click me to go to ggj</a>'*/
    }
})