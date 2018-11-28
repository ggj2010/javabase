<!DOCTYPE html>
<html ng-app="iamge">
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
<#assign path=request.contextPath />
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<link href="https://cdn.bootcss.com/blueimp-gallery/2.21.3/css/blueimp-gallery.css" rel="stylesheet">
<script src="https://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="https://cdn.bootcss.com/blueimp-gallery/2.21.3/js/blueimp-gallery.min.js"></script>
<script src="https://cdn.bootcss.com/blueimp-gallery/2.21.3/js/jquery.blueimp-gallery.min.js"></script>
<script src="https://cdn.bootcss.com/angular.js/1.5.0/angular.js"></script>
<script src="https://ggjcdn.oss-cn-shanghai.aliyuncs.com/ng-infinite-scroll.min.js"></script>
<style>
    .lightBoxGallery img {
        margin: 5px;
        width: 160px;
    }
</style>
<#assign base=request.contextPath />
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
        <div class="row" ng-controller='scrollController'>
            <div infinite-scroll='reddit.nextPage()' infinite-scroll-disabled='reddit.busy'
                 infinite-scroll-distance='0'>
                <div ng-repeat='object in reddit.links track by $index'>
                    <div ng-repeat='image in object.images'>
                        <div class="col-md-3 col-xs-6 col-sm-4">
                            <div class="thumbnail">
                                <a href="{{image}}" title="{{object.title}}" data-gallery>
                                    <img ng-src="{{image}}">
                                </a>
                                <div class="caption">
                                       <p>{{object.title}}-{{object.text}}</p>
                                       <p>
                                           <a class="btn btn-link" role="button"  ng-click="attention(object.email,object.userId)" >关注</a>
                                           <a class="btn btn-link" role="button"  ng-click="chart(object.userId)" >记录</a>
                                       </p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div style='clear: both;'></div>
                </div>
                <div ng-show='reddit.busy'>Loading data...</div>
            </div>
        </div>
    </div>
    <input type="text" id="text"/>
    <script>
        var iamge = angular.module('iamge', ['infinite-scroll']);

        iamge.controller('scrollController', function ($scope, $http,Reddit) {
            $scope.reddit = new Reddit();
            $scope.attention=function (email,userId) {
                var copy=document.getElementById("text");
                copy.value=email.replace("@qq.com","");
                copy.select(); //选择对象
                var tag = document.execCommand("Copy");
                if(confirm("是否关注")){
                    var url="${path}/cat/attention?userId="+userId;
                    $http.get(url).success(function (data) {
                    }.bind(this));
                }
            }
            $scope.chart=function (userId) {
                window.open("${path}/cat/chart?userId="+userId);
            }
        });

        // Reddit constructor function to encapsulate HTTP and pagination logic
        iamge.factory('Reddit', function ($http) {
            var Reddit = function () {
                this.links = [];
                this.busy = false;
                this.after = 0;
            };

            Reddit.prototype.nextPage = function () {
                if (this.busy) return;
                this.busy = true;
                var that = this;
                var page = this.after;
                var url = "${path}/cat/page?page="+page;
                $http.get(url).success(function (data) {
                    $.each(data, function (key, obj) {
                        var object = new Object();
                        object.title =  obj.ma_title;
                        object.text =  obj.ma_text;
                        object.userId =  obj.mu_id;
                        object.email =  obj.mu_email;
                        object.images = obj.ma_images;
                        that.links.push(object);
                    });
                    this.after = this.after + 1;
                    this.busy = false;
                }.bind(this));
            };
            return Reddit;
        });
    </script>
</body>
<script src="https://ggjcdn.oss-cn-shanghai.aliyuncs.com/statistics.js"></script>
</html>
