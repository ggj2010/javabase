<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户信息</title>
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
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<link href="https://cdn.bootcss.com/blueimp-gallery/2.21.3/css/blueimp-gallery.css" rel="stylesheet">
<script src="https://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="alert alert-success" role="alert">个人信息</div>
        <div class="col-sm-6 col-md-4">
            <div class="thumbnail">
                <img src="${(headimgurl)?if_exists }" alt="" width="300px" height="300px" />
                <div class="caption">
                    <h3>${(name)?if_exists }</h3>
                   <address>
                    <strong>${(country)?if_exists }</strong><br>
                      ${(province)?if_exists }市<br>
                     ${(city)?if_exists }省<br>
                    <abbr title="性别"></abbr> <span class="glyphicon glyphicon-user"> ${(sex)?if_exists }</span>
                </address>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script src="https://ggjcdn.oss-cn-shanghai.aliyuncs.com/statistics.js"></script>
</html>


