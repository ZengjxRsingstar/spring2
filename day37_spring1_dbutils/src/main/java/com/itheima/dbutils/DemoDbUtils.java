package com.itheima.dbutils;

import com.itheima.domain.Account;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class DemoDbUtils {
    private static ComboPooledDataSource dataSource = new ComboPooledDataSource();

    private static QueryRunner runner = new QueryRunner(dataSource);

    @Test
    public void test1() throws SQLException {
        //2. 使用JdbcTemplate的方法执行SQL语句
        List<Account> accountList = runner.query("select * from account", new BeanListHandler<Account>(Account.class));
        for (Account account : accountList) {
            System.out.println(account);
        }
    }

    @Test
    public void test2() throws SQLException {
        int update = runner.update("insert into account (id,name,money) values (?,?,?)", null, "jack", 2000f);
        System.out.println("影响行数：" + update);
    }
}
