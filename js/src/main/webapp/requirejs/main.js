//如果引入页面 加入data-main属性，那么paths就不用写了，直接引用当前requier.js所在的路径/+引用名称
require.config({
    baseUrl:'requirejs',
    paths: {
        jquery:'lib/jquery-2.1.4.min',
        bootstrap: 'bootstrap-3.3.5.min',
        a:"js/a",
        b:"js/b"
    },

    //依赖关系bootstrap依赖jquery
    shim : {
        //每个模块要定义（1）exports值（输出的变量名），表明这个模块外部调用时的名称；（2）deps数组，表明该模块的依赖性。
        bootstrap : {
            deps : ['jquery'],
            exports :'bootstrap'
        }


    }
});





