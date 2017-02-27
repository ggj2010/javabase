package com.ggj.db.splittable.dao;

import org.apache.ibatis.annotations.Param;

/**
 * @author:gaoguangjin
 * @date 2017/2/22 14:23
 */
public interface CreateTableMapper {
    void createOrderTable(@Param("tableName") String tableName);
}
