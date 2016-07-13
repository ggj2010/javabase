package com.ggj.encrypt.common.plugin;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.ggj.encrypt.common.utils.mybatis.MyAbstractRoutingDataSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * @author:gaoguangjin
 * @date 2016/7/13 11:27
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class,Object.class})}
)
public class MyBatisCatPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = null;
        Transaction transaction=null;
        MappedStatement mappedStatement= (MappedStatement) invocation.getArgs()[0];
        Object objects= (Object) invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(objects);
//        MyAbstractRoutingDataSource myAbstractRoutingDataSource = (MyAbstractRoutingDataSource) mappedStatement.getConfiguration().getEnvironment().getDataSource();
        try{
            transaction = Cat.getProducer().newTransaction("SQL", mappedStatement.getId());
            result = invocation.proceed();
            Cat.getProducer().logEvent("SQL.Method", mappedStatement.getSqlCommandType().name(),Message.SUCCESS,"");
//            Cat.getProducer().logEvent("SQL.Database","" ,Message.SUCCESS,"");
            transaction.setStatus(Message.SUCCESS);
            transaction.addData(boundSql.getSql().trim().replaceAll("\\n",""));
        }catch (Exception e){
            transaction.setStatus(e);
        }finally {
            transaction.complete();
        }
        return result;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
