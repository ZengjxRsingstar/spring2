package com.itheima.test;

import com.itheima.config.AppConfig;
import com.itheima.dao.UserDao;
import com.itheima.service.UserService;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.crypto.interfaces.PBEKey;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SpringAnnotationTest {

    @Test
    public void test1(){
        ApplicationContext app = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = (UserService) app.getBean("userService");
        userService.save();
    }

    @Test
    public void test2(){
        ApplicationContext app = new AnnotationConfigApplicationContext(AppConfig.class);

        AppConfig appConfig = (AppConfig) app.getBean("appConfig");
        //appConfig.show();
    }

    //获取连接池对象
    @Test
    public void test3() throws SQLException {
        ApplicationContext app = new AnnotationConfigApplicationContext(AppConfig.class);

        DataSource dataSource = app.getBean("ds", DataSource.class);
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        connection.close();
    }

    @Test
    public void test4() throws SQLException {
        ApplicationContext app = new AnnotationConfigApplicationContext(AppConfig.class);
        QueryRunner runner = app.getBean("runner", QueryRunner.class);
        List<Map<String, Object>> mapList = runner.query("select * from account", new MapListHandler());

        for (Map<String, Object> map : mapList) {
            System.out.println(map);
        }
    }
}
