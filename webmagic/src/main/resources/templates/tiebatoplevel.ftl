<!DOCTYPE html>
<html  ng-app="echart">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
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
<script src="https://cdn.bootcss.com/angular.js/1.5.0/angular.js"></script>
<script src="https://cdn.bootcss.com/echarts/3.2.2/echarts.js"></script>
<script src="https://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<body>
<div class="container">
    <div class="row" ng-controller="onclickController">
        <div class="col-md-3">
         <button ng-click="filter.show()" class="btn btn-info">点击显示图表</button>
        </div>
        <div class="col-md-9">
            <div class="panel panel-info">
                <div class="panel-heading">图表</div>
                <div class="panel-body">
                    <echart-demo id="main" style="width: 600px;height:400px;" filter="filter"></echart-demo>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">
    var app = angular.module('echart', []);
    var xAxis = [], series = [];
    <#list xAxis as xis>
    xAxis.push("${xis}")
    </#list>
    <#list series as ies>
    series.push(${ies ?c})
    </#list>
    app.controller('onclickController', function ($scope) {
        //定义filter 实现父调用子类
        $scope.filter = {};
    });
    app.directive('echartDemo', function () {
        return {
            restrict: 'E',
            scope: {
                filter: "="
            },
            link: function (scope, element) {
                var myChart = echarts.init(element[0]);
                var option = {
                    title: {
                        text: '${name}贴吧等级分组人数'
                    },
                    tooltip: {},
                    legend: {
                        data: ['等级']
                    },
                    xAxis: {
                        data: xAxis
                    },
                    yAxis: [
                        {
                            type: 'value',
                            name: '人数'
                        }
                    ],
                    dataZoom: [
                        {
                            show: true,
                            start: 0,
                            end: 100
                        },
                        {
                            type: 'inside',
                            start: 50,
                            end: 100
                        },
                        {
                            show: true,
                            yAxisIndex: 0,
                            filterMode: 'empty',
                            width: 30,
                            height: '80%',
                            showDataShadow: false,
                            left: '93%'
                        }
                    ],
                    series: [{
                        name: '等级',
                        type: 'bar',
                        data: series
                    }]
                };
                scope.filter.show = function () {
                    myChart.setOption(option);
                };
            }
        };
    });
</script>
</html>