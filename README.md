# java学习记录一些东西和teamstudy类似
    整个项目比较乱，模块分的也比较随意，这些只是自己在空闲的时候用来review 代码的。
## 模块
      ```
            <module>thread</module>
            <module>socket</module>
            <module>lambada</module>
            <module>kafka</module>
            <module>zookeeper</module>
            <module>redis</module>
            <module>js</module>
            <module>httpclient</module>
            <module>mobiletechnology</module>
             <module>webmagic</module>
      ```
     目前有这几个模块，以后可能会有新的模块添加进去的
###thread模块

 这个模块主要用来记录一些多线程基础。具体干什么的代码里面都有注释
###httpclient模块
  这个模块是用来记录怎么使用httpclient这个工具，以及新老版本的切换和api调用，同时也写了一个demo，用来爬取邮乐网的用户地址信息，
  模拟用户登陆，然后抓取用户收货地址，保存到redis.当时是有那么一个漏洞，可以访问任意用户的收货地址
###mobiletechnology模块
    因为目前在手机组，刚好公司的某个手机后台api项目需要重构，于是自己也搭建了一套。
  api后台模块设计可以参考这个人的博客：http://blog.csdn.net/newjueqi/article/details/19003775
  目前完成的功能是利用springboot+redis 搭建的一套restful风格的api
  总结下几个核心的功能：springboot的自定义拦截和全局异常处理 包括返回值code设计，和一些简单的参数加密和签名校验。
  以后准备引入dubbo+kafka。dubbo的源码看了一段时间又放下了，下个星期来了继续研读。
  --2016-04-29
###webmagic模块
    有意思的爬虫

