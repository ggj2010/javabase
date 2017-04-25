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
<script  src="https://cdn.bootcss.com/echarts/3.2.2/echarts.js"></script>
<script src="https://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<body>
<div ng-controller="onclickController" class="container">
            <button ng-click="filter.show()" class="btn btn-info">点击显示图表</button>
        <echart-demo id="main" style="width: 600px;height:400px;"  filter="filter">
    </echart-demo>
</div>
</body>
<script type="text/javascript">
    var app = angular.module('echart', []);
    var xAxis=[],series=[];
    <#list xAxis as xis>
         xAxis.push("${xis}")
    </#list>
    <#list series as ies>
        series.push(${ies})
    </#list>
    app.controller('onclickController',function($scope){
        //定义filter 实现父调用子类
        $scope.filter = {
        };
    });
    app.directive('echartDemo', function() {
        return {
            restrict: 'E',
            scope:{
                filter: "="
            },
            link: function(scope, element) {
                var myChart = echarts.init(element[0]);
                var option = {
                    title: {
                        text: '${name}贴吧Top排行'
                    },
                    tooltip: {},
                    legend: {
                        data:['经验值']
                    },
                    xAxis: {
                        data: xAxis
                    },
                    yAxis: [
                        {
                            type : 'value',
                            name : '经验'
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
                        name: '经验值',
                        type: 'bar',
                        data: series
                    }]
                };
                scope.filter.show=function(){
                    myChart.setOption(option);
                };
            }
        };
    });
</script>
<script src="https://ggjcdn.oss-cn-shanghai.aliyuncs.com/statistics.js"></script>
</html>