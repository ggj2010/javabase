# 递增写一个rpc框架

## 1、基础版本（first）
    最简单的就是基于socket自己封装一个
    优点：快速简单
    缺点：socket的笨重、不支持并发
## 2、基础升级版本(second)
     基于socket+动态代理
     优点：封装client端调用细节、server端多线程处理请求
     缺点：不支持异步调用
## 3、异步调用版本(ansyc)
![流程图](http://school-tiku.oss-cn-shanghai.aliyuncs.com/homework/1567504033079/WX20190903-174439%402x.png)

     异步调用，返回值在用到时候才去主动获取
     思路：返回值肯定是一个future,这样才能实现异步，需要设计一个requestId,这样才能实现当客户端收到返回值当时候，
     可以将返回值放到指定future里面，请求时候和返回时候不在同一个thread里面，Future不能放到ThreadLocal，只能
     将requestId放到ThreadLocal里面

## 4、封装socket(netty-first)
      通过SecondBenchMarkTest压测，v2版本的qps才740，性能绝大部分卡在server和client 端的socket的创建与销毁，
      基于netty版本的；压测qps可以达到6万多
      所以我们可以利用netty实现长连接版本简单的rpc
      功能：1、增加注解自动扫描服务
           2、增加protobuf序列化
           3、客户端调用增加超时时间

      难点：1、客户端长链接如何处理返回值（依旧是熟悉的future）
           2、解决粘包拆包（参考ClientEncoder 设计自定义的数据传输协议 （数据byte长度+4）+ 数据Byte作为一个数据包）

## 5、注册中心(netty-zk)
      服务注册与发现封装,解决单点问题
      功能点：
      难点：1、zk树注册节点设计
           2、服务的注册
            （例如客户端连接到服务端A,这时候新上线了个服务B,客户端如何连接到这个服务B）
           3、服务的发现
           （例如客户端连接到服务端A,这时候服务端A下线了，客户端如何平滑切流量）
             1、监控zk节点 移除客户端连接A服务端的连接
             2、当前正在访问的链接每次请求之前都判断 channel.active()判断socket是否在线
             （但是还是无法解决 正在发起请求的客户端被下线，超时业务需要自己补偿）
             3、定时pin
           4、客户端如何实现负载均衡
             本例只是简单基于随机法实现，其他的还有轮询法、加权轮询法、请求次数等

           5、如何实现缓存高性能
            className -> ipList
            ip-> 多个链接 (删除机器时候 客户端可以自动发现并且删除链接)
            ip->classNameSet(增加机器时候 客户端可以自动发现并且添加链接)
           6、服务下线
            shutdown hook
      server 是用来记录注册的服务里面对应的机器ip,多个ip以逗号分割，例如（192.168.8.103:4081,192.168.8.103:4082）
      app 是用来记录提供服务的名称
      weight 是用来记录提供服务机器的权重
      --ggjRPC
      ----server
      -------com.ggj.java.rpc.demo.netty.first.server.service.AppleService
      -------com.ggj.java.rpc.demo.netty.first.server.service.OrangeService
      ----app
      -------usezkService
      ----weight

      客户端可以一次性和服务端建立多个连接 提高并发效率

## 6、利用spring来包装rpc

    基于Spring的注解实现rpc的注册和rpc的发现
    rpc框架该有的功能点
    1、服务端
      注册url:
      分组：提供接口多实现功能
      版本号：当一个接口的实现，出现不兼容升级时或者处理逻辑变化时候，可以用版本号过渡
    2、客户端：
      超时时间功能
      负载均衡方式：
      分组
      版本号
      序列化方式
      同步和异步
      结果返回时候回掉
      超时重试
    3、spi机制
    框架要提供扩展接口，例如dubbo的注册中心可以选用redis或者dubbo
    服务可以用netty和http











