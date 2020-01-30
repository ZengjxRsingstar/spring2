package com.itheima.dao.impl;

import com.itheima.dao.UserDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//@Component("userDao")
@Repository("userDao")
@Scope("prototype")
public class UserDaoImpl implements UserDao {

    public UserDaoImpl() {
        System.out.println("UserDaoImpl()构造方法执行了");
    }

    //对象构造成功之后执行的方法，相当于bean标签的init-method
    @PostConstruct
    public void initMethod(){
        System.out.println("initMethod()");
    }

    //对象销毁之前执行的方法，相当于bean标签的destroy-method
    @PreDestroy
    public void destroyMethod(){
        System.out.println("destroyMethod()");
    }

    @Override
    public void save() {
        System.out.println("UserDaoImpl.save()");
    }
}
