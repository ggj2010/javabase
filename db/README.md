#关于数据库的一些记录

#### jdbc常识

* connection.createStatement与connection.prepareStatement区别
    * sql预编译可以防止sql注入
    * 无论mysql还是oracle都有sql预编译，已经编译检查过的sql,会缓存到内存，大大提供执行效率
    * 参考代码：TeamStudy项目 com.team.gaoguangjin.jdbc.normal.NormalJDBC
* 原生jdbc和jdbc-pool区别与适用场景
    * 用pool的方式获取connection，因为获取connection是需要消耗一定性能的，所以一般web项目都用druidpool
    * 一定批处理任务，如对账，清结算，都是固定连接数的同时处理大批量数据的可以使用原生的jdbc
    
#### 分库分表 
        在同一数据库中，对多个表进行并发更新的效率要远远大于对一个表进行并发更新
  * 水平与垂直切分
   * 水平切分：把表的数据按某种规则（比如按ID散列）切分到多张表或者多个数据库(server)上
   * 垂直切分：把关系紧密（比如同一模块）的表切分出来放在一个server上（普遍的用法）
  * 可参考博客
    * [http://www.jianshu.com/p/32b3e91aa22c?from=timeline](http://www.jianshu.com/p/32b3e91aa22c?from=timeline)
    * [http://www.cnblogs.com/chy2055/p/5125245.html](http://www.cnblogs.com/chy2055/p/5125245.html)
    * [每秒处理10万订单乐视集团支付架构](http://mp.weixin.qq.com/s?__biz=MjM5MjAwODM4MA==&mid=2650686445&idx=1&sn=9117ee33bff27b128a287a6c751d3e32&scene=0#rd)
    * [数据库分库分表(sharding)](http://blog.csdn.net/column/details/sharding.html)
 * 只分库，不分表
 * 不分库，只分表
 * 分库同时分表
 ![分库效果库](http://ocg3iebmc.bkt.clouddn.com/640.webp.jpg "分库效果库")
 * 分表规则
    * 根据指定id进行取模，比如 数据库编号 = (uid / 10) % 8 + 1   表编号 = uid % 10
#### 分库分表需要考虑的问题
   * 事务问题
   * 跨节点Join count,order by,group by以及聚合函数的问题
#### 分库分表实现
* 组件
    * sharding-jdbc
       * wiki: [http://dangdangdotcom.github.io/sharding-jdbc/post/user_guide/](http://dangdangdotcom.github.io/sharding-jdbc/post/user_guide/)
    * TSharding
* 中间件
    * mycat
     
#### 是否真的需要分库分表

       如果单表的数量超过千万或者接下来的业务增长导致数据库数据暴增单机无法满足了，再考虑分片。
       如果数据量少于千万，我们可以考虑使用数据库自带的表分区功能。
       如果数据有明显冷热区别，可以进行冷热分离。冷的数据可以异步从热的数据里面同步到mongodb


