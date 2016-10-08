<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>elasticsearch 查询</title>
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
<style>
</style>
<body>


<div class="container-fluid">
    <div id="links" class="lightBoxGallery">
        <div class="row">
            <table class="table table-bordered">
                <tr>
                    <th>帖子地址</th>
                    <th>贴吧名称</th>
                    <th>帖子作者</th>
                    <th>标题</th>
                    <th>日期</th>
                </tr>
            <#if listContent??&&listContent?size gt 0>
                <#list listContent as data>
                <tr>
                    <td><a href="${pageUrlPrefix}${data.id}">传送门</a> </td>
                    <td>${(data.name)?if_exists }</td>
                    <td>${(data.authorName)?if_exists }</td>
                    <td>${(data.title)?if_exists }</td>
                    <th>${(data.date)?if_exists }</th>
                </tr>
                </#list>
            </#if>
            </table>
    </div>
    </div>
</div>


<script>

</script>
</body>
</html>