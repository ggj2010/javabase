<!DOCTYPE html>
<html ng-app="search">
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
<script src="https://cdn.bootcss.com/angular.js/1.5.0/angular.js"></script>
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<link href="https://cdn.bootcss.com/blueimp-gallery/2.21.3/css/blueimp-gallery.css" rel="stylesheet">
<script src="https://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<#assign base=request.contextPath />
<style>
</style>
<body>

<div class="container-fluid">
    <div class="panel panel-info">
        <div class="panel-heading">
            帖子查询
        </div>
            <div class="panel-body">
                <div id="links" class="lightBoxGallery">
                    <div class="row">
                        <table class="table table-bordered table-responsive">
                            <tr>
                                <th>帖子地址</th>
                                <th class="visible-lg">贴吧名称</th>
                                <th class="visible-lg">帖子作者</th>
                                <th>标题</th>
                                <th class="visible-lg">日期</th>
                            </tr>
                        <#if listContent??&&listContent?size gt 0>
                            <#list listContent as data>
                            <tr>
                                <td><a href="${pageUrlPrefix}${data.id}" class="btn btn-default" target="_blank">传送门</a> </td>
                                <td class="visible-lg">${(data.name)?if_exists }</td>
                                <td class="visible-lg">${(data.authorName)?if_exists }</td>
                                <td title="${(data.title)?if_exists }">${(data.title)?if_exists }</td>
                                <td class="visible-lg">${(data.date)?if_exists }</td>
                            </tr>
                            </#list>
                        </#if>
                        </table>
                    </div>
                    <ul class="pager" ng-controller="pageController">
                        <a class="btn btn-danger" href="javascript:history.go(-1);">返回主页</a>
                        <span class="page-list">共${totalSize}</span>条记录
                        <li>
                            <a href="" aria-label="Previous" name="pages"
                               ng-click="page(${from-1})"> <span aria-hidden="true">上一页</span>
                            </a>
                        </li>
                        <li>
                            <a href="" aria-label="Next" name="pages"
                               ng-click="page(${from+1})" > <span aria-hidden="true">下一页</span>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        <div>
    </div>
</div>
<script>
    var app = angular.module('search', []);
    app.controller("pageController",function ($scope) {
        $scope.page=function (i) {
            if(i<0){
                i=0;
            }
            window.location.href="${base}/tieba/search?keyWord=${keyWord}"+"&from="+i;
        }
    });
</script>
<script src="https://ggjcdn.oss-cn-shanghai.aliyuncs.com/statistics.js"></script>
</body>
</html>