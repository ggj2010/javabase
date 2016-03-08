//若是模块没有依赖，但是需要用一个函数做一些初始化工作，然后定义自己通过define的匿名函数
define(function(){
    return {color:"black",name:"a"}
});