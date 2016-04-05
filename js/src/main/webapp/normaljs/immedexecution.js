//立即执行js

var immdeiately= function(){
    alert("立即执行js");
};
immdeiately();

//// 这么写会报错，因为这是一个函数定义：
//function(){}();
(function(){
alert("(function(){})")
});
//
// 常见的（多了一对括号），调用匿名函数：
(function(param){
    alert( "但在前面加上一个布尔运算符（只多了一个感叹号），就是表达式了，将执行后面的代码，也就合法实现调用  (function(){})()"+param)
})(123);

//但在前面加上一个布尔运算符（只多了一个感叹号），就是表达式了，将执行后面的代码，也就合法实现调用
!function(param){
  alert( "但在前面加上一个布尔运算符（只多了一个感叹号），就是表达式了，将执行后面的代码，也就合法实现调用  !function(){}()"+"参数符号"+param)
}("$");