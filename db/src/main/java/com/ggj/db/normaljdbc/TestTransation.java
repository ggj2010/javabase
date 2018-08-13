
package com.ggj.db.normaljdbc;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * 事物
 * @author:gaoguangjin
 * @date:2018/5/23
 */
@Slf4j
public class TestTransation {
    public static void main(String[] args) {
        //开启事物
        reviewTransation(true);
        //不开启事物
        //reviewTransation(false);
    }

    private static void reviewTransation(boolean useTransation) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/article2?useUnicode=true&characterEncoding=utf-8",
                    "", "");
            // 开启事物
            connection.setAutoCommit(!useTransation);
            insert(connection);
            connection.rollback();
        } catch (SQLException e) {
            if (useTransation) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    log.error("rollback error", e);
                }
            }
        } finally {
            if (useTransation) {
                try {
                    connection.commit();
                } catch (SQLException e) {
                    log.error("commit error", e);
                }
            }
        }

    }

    private static void insert(Connection connection) {
        try {
            for (int i = 1; i < 3; i++) {

                PreparedStatement preparedStatement = connection
                        .prepareStatement("INSERT  INTO  test (name,age) VALUES (?,?)");
                preparedStatement.setString(1, "name");
                preparedStatement.setInt(2, 30);
                int count = preparedStatement.executeUpdate();

                //id自增
                PreparedStatement preparedStatementSelect = connection
                        .prepareStatement("INSERT  INTO  test (name,age) VALUES (?,?)");
                ResultSet resultSet = preparedStatementSelect.executeQuery("SELECT LAST_INSERT_ID() from test");

                while (resultSet.next()){
                    log.info("last autoincrease id={}" ,resultSet.getInt(1));
                }
                resultSet.close();
            }
        } catch (Exception e) {
            log.error("error", e);
        }
    }
}
