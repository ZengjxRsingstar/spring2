package com.itheima.dao.impl;

import com.itheima.dao.UserDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

    @Override
    public void save() {
        System.out.println("UserDaoImpl.save()");
    }
}
