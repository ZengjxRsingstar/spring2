package com.itheima.test;

import com.itheima.dao.UserDao;
import com.itheima.service.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.crypto.interfaces.PBEKey;

public class SpringAnnotationTest {

    @Test
    public void test1(){
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        //Object userDao = app.getBean("userDao");
        //System.out.println(userDao);

        UserService userService = (UserService) app.getBean("userService");
        //System.out.println(userService);

        userService.save();
    }

    @Test
    public void test2(){
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        UserDao userDao = app.getBean("userDao", UserDao.class);
        System.out.println(userDao);

        UserDao userDao3 = app.getBean("userDao", UserDao.class);
        System.out.println(userDao3);

        ((ClassPathXmlApplicationContext) app).close();
    }
}
