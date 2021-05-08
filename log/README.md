##日志的一些记录



#### 日志框架大杂烩

##### 日志框架门面接口

    commons-logging（jcl）和slf4j

##### 日志框架实现

    log4j1 和logback java.util.logging等

##### slf4j-api日志架包

    slf4j-api：定义了sl4j的接口
    只适用于 Logger log= LoggerFactory.getLogger(Sl4jTest.class);
* sl4j绑定logback


        logback-classic :实现sl4j到logback的桥接器
* sl4j绑定log4j


        slf4j-log4j12 ：slf4j到log4j的桥接器

##### 其它日志框架API转调回slf4j
       实现jcl、jul、log4j转换成 sl4jApi形式
     <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>jcl-over-slf4j</artifactId>
        <version>${slf4j.version}</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>jul-to-slf4j</artifactId>
        <version>${slf4j.version}</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>log4j-over-slf4j</artifactId>
        <version>${slf4j.version}</version>
    </dependency>

    比如原来项目里面是使用log4j的，这时候想切换到sl4j,那么在原来代码写法不变的情况下，
    只用去除
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>
    然后引入就是平滑切入到sl4j形式打印日志
         <dependency>
                <groupId>org.slf4j</groupId>
                <artifactId>log4j-over-slf4j</artifactId>
                <version>${slf4j.version}</version>
         </dependency>

##### 日志使用的两种写法
    Logger log= Logger.getLogger(Log4JTest.class)
    1.如果底层想使用log4j,直接引入log4j架包
    2.如果底层想使用sl4j的log4j,直接引入 slf4j-log4j12架包，然后引入log4j-over-slf4j
    2这种做法一般不会的，本来用的就是log4j写法 底层再转换成sl4j的log4j实现

    3.如果底层想使用sl4j的logback,直接引入 logback-classic架包，然后引入log4j-over-slf4j

    //使用sl4j的工厂获取loger
    Logger log= LoggerFactory.getLogger(Sl4jTest.class);
    1、如果底层想使用logback,直接引入 logback-classic架包，
    2、如果底层想使用log4j，直接引入 slf4j-log4j12架包

    总结：无论是使用log4j 还是logback 最好写法都是使用LoggerFactory.getLogger这种形式
    因为**-over-slf4j 都是为了一些老项目转换成 slfj形式


##### log4j2
    lo4j2比log4j、logback性能都高，log4j只支持同步写，而log4j2 支持异步写，提供性能10倍
    依赖jar
         <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.5</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.5</version>
        </dependency>
    使用方法：
      Logger log= LogManager.getLogger(Log4j2.class);

##### sl4j转换成log4j2
       去除所有依赖的sl4j，例如logback,log4j
       然后引入 log4j-slf4j-impl
       <dependency>
           <groupId>org.apache.logging.log4j</groupId>
           <artifactId>log4j-slf4j-impl</artifactId>
           <version>2.5</version>
       </dependency>

#### 日志级别
    TRACE < DEBUG < INFO < WARN < ERROR


