#写一些工作之外的代码

## copybean
        其实工作当中经常遇到VO与bean之间转换的操作，比如查询数据库返回的bean肯能有10多个字段，
    但是接口返回可能只需要5个字段VO，如果一个一个set肯定很麻烦，spring有个类叫BeanUtils，
    BeanUtils.copyProperties()可以进行对象之间的拷贝，spring BeanUtils类也做了一些本地缓存
    ，这样拷贝的时候就可以减少反射调用的次数，提高拷贝性能。

## overridparam

        经常看到一些项目的spring配置文件，你全局搜了半天可能也搜不到${ddddd}的值
    <property name="timeout" value="${ddddd}" />
    spring可以重写PropertyPlaceholderConfigurer类，在解析占位符的时候我们可以
    加上自己的解析逻辑，如果公司里面有统一配置中心，这时候就可以调用配置中心，获取dddd
    的值。springcloud-config 应该也是类似的原理吧，因为它的值都是从config-server里面拿的
## clientapi
        当我们调用某个基础服务的时候，一般基础服务组都会对外提供一个client jar给我吗调用，
    client里面封装了调用基础服务的一些细节问题，比如初始化配置信息，并发，还有一些设计模式
    的写法，都需要考虑。
            
  
        
        
             