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
  可参考博客：
              [http://www.jianshu.com/p/32b3e91aa22c?from=timeline](http://www.jianshu.com/p/32b3e91aa22c?from=timeline)      
              [http://www.cnblogs.com/chy2055/p/5125245.html](http://www.cnblogs.com/chy2055/p/5125245.html)      
              [每秒处理10万订单乐视集团支付架构](http://mp.weixin.qq.com/s?__biz=MjM5MjAwODM4MA==&mid=2650686445&idx=1&sn=9117ee33bff27b128a287a6c751d3e32&scene=0#rd)      
 * 只分库，不分表
 * 不分库，只分表
 * 分库同时分表
 ![分库效果库](http://ocg3iebmc.bkt.clouddn.com/640.webp.jpg "分库效果库")
 * 分表规则
    * 根据指定id进行取模，比如 数据库编号 = (uid / 10) % 8 + 1   表编号 = uid % 10
    
       

