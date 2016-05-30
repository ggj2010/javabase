package com.ggj.encrypt.configuration;

import com.ggj.encrypt.common.utils.SpringContextHolder;
import com.ggj.encrypt.common.utils.mybatis.DataSourceType;
import com.ggj.encrypt.common.utils.mybatis.MyAbstractRoutingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author:gaoguangjin
 * @date 2016/5/30 11:18
 */
@Configuration
@AutoConfigureAfter({ DataSourceConfiguration.class })
@Slf4j
public class MybatisConfiguration  extends MybatisAutoConfiguration {
    @Value("${datasource.size}")
    private String dataSourceSize;

    @Bean
    public SqlSessionFactory sqlSessionFactorys() throws Exception {
        log.info("-------------------- 重载父类 sqlSessionFactory init ---------------------");
        return super.sqlSessionFactory(roundRobinDataSouceProxy());
    }

    /**
     * 有多少个数据源就要配置多少个bean
     * @return
     */
    @Bean
    public AbstractRoutingDataSource roundRobinDataSouceProxy(){
        int size=Integer.parseInt(dataSourceSize);
        MyAbstractRoutingDataSource proxy = new MyAbstractRoutingDataSource(size);
        Map<Object, Object> targetDataSources=new HashMap<Object, Object>();
        DataSource writeDataSource =SpringContextHolder.getBean("writeDataSource");
        //写
        targetDataSources.put(DataSourceType.write, SpringContextHolder.getBean("writeDataSource"));

        for (int i = 0; i <size ; i++) {
            targetDataSources.put(i,SpringContextHolder.getBean("readDataSource"+(i+1)));
        }
        proxy.setDefaultTargetDataSource(writeDataSource);
        proxy.setTargetDataSources(targetDataSources);
        return proxy;
    }










}
