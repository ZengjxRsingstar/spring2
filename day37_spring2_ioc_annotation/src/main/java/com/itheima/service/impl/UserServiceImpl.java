package com.itheima.service.impl;

import com.itheima.dao.UserDao;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

//@Component("userService")
@Service("userService")
@Scope("singleton")
public class UserServiceImpl implements UserService {

    /*
        @Autowired：byType注入
            * 如果UserDao类型只有一个，直接注入
            * 如果UserDao类型有多个bean对象存在，byName注入
        @Qualifier：和@Autowired配合使用
            * 用于在byType基础上，直接byName注入

        @Resource：相当于@Autowired + @Qualifier
     */
    @Autowired
    @Qualifier("userDao")
    /*@Resource(name = "userDao2")*/
    private UserDao userDao;

    @Override
    public void save() {
        userDao.save();
    }
}
