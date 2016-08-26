<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>吧友图片</title>
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
<script src="http://cdn.bootcss.com/blueimp-gallery/2.21.3/js/blueimp-gallery.min.js"></script>
<script src="http://cdn.bootcss.com/blueimp-gallery/2.21.3/js/jquery.blueimp-gallery.min.js"></script>
<style>
    .lightBoxGallery img {
        margin: 5px;
        width: 160px;
    }
</style>
<body>


<!-- The Gallery as lightbox dialog, should be a child element of the document body -->
<div id="blueimp-gallery" class="blueimp-gallery">
    <div class="slides"></div>
    <h3 class="title"></h3>
    <a class="prev">‹</a>
    <a class="next">›</a>
    <a class="close">×</a>
    <a class="play-pause"></a>
    <ol class="indicator"></ol>
</div>
<div class="container-fluid">
    <div id="links" class="lightBoxGallery">
        <div class="row">
    <#if data??&&data?size gt 0>
        <#list sortPageIds as key>
            <#list data[key] as image>
           <div class="col-md-3 col-xs-6 col-sm-4">
                <div class="thumbnail">
                   <a href="${image}" title="吧友图片"  data-gallery>
                        <img src="${image}" >
                    </a>
                    <div class="caption">
                       <p> <a class="btn btn-link" role="button"  href="${pageUrlPrefix}${key}" title="点击跳转到对应帖子">传送门</a></p>
                    </div>
                   </div>
            </div>
             </#list>
        </#list>
     </#if>
    </div>
    </div>
</div>
</body>
</html>