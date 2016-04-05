
//若是模块具有依赖关系，第一个参数应该是一个数组，其项目为依赖名字，第二个参数是匿名函数

//匿名函数在依赖项加载结束后会立即加载，函数会返回一个对象用以定义这个模块。

//前面的依赖项将以参数的形式传递给函数，顺序与之前一致。

//再看我们的例子，一个球衣模块被创建了（我们返回的是一个衣服模块
// require(["module/name", ...], function(params){ ... });
define(["jquery","a"],function($,a){
    var name= a.name+"b";
    $("#b").on("click",function(){
        alert("b模块被调用了,b.js里面写了onclik事件");
    });
    return {
      getName:function(){
          return name;
      }
    }
})