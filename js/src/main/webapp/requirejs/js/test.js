require(["jquery","b"],function($,b){
    $(function(){
      $("#b").on("click",function(){
          alert("返回a模块和b模块："+b.getName());
      })
    })
})