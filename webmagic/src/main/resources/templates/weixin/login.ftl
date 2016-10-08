<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>微信扫码登录</title>
    <meta name="keyworlds" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Pragma" content="no-cache">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="layoutmode" content="standard">
</head>
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<link href="http://cdn.bootcss.com/blueimp-gallery/2.21.3/css/blueimp-gallery.css" rel="stylesheet">
<script src="http://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="//cdn.bootcss.com/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
<#assign base=request.contextPath />
<body>
<div class="container-fluid">
        <div class="row">
            <div class="col-sm-6 col-md-4">
                <div class="thumbnail">
                    <div class="caption">
                        <h3>扫码登录</h3>
                    </div>
                    <div id="qrcode-container">
                    </div>
                </div>
            </div>
        </div>
</div>
</body>
<script>
    var websocket = null;
    $(function () {
        //判断当前浏览器是否支持WebSocket
        if ('WebSocket' in window) {
            websocket = new WebSocket("ws://${websocketUrl}/weixin/login/state");
        }
        else {
            alert('Not support websocket')
        }
        //连接发生错误的回调方法
        websocket.onerror = function () {
            console.log("error");
        };

        //连接成功建立的回调方法
        websocket.onopen = function (event) {
            console.log("open");
        }

        //接收到消息的回调方法
        websocket.onmessage = function (event) {
            var data = JSON.parse(event.data);
            if(data.code=="0000"){
                window.location.href="${base}/weixin/center?token="+data.data;
            }

        }

        //连接关闭的回调方法
        websocket.onclose = function () {
            console.log("close");
        }

        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = function () {
            websocket.close();
        }

        $('#qrcode-container').qrcode("${url}");

             setTimeout(connet,2000);
    })

    function connet() {
        websocket.send("${token}");
    }

</script>
</html>