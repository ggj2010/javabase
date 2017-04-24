var test={
    init: function() {
        this.initData();
    },
    initData:function () {
        test.testcallback(function (number) {
            alert(number);
        })
    },
    
    testcallback:function (callback) {
        callback("这是回调");
    }
}