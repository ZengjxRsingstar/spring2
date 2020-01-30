package com.itheima.test;

import com.itheima.domain.Account;
import com.itheima.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;
import java.util.List;

public class SpringCurdTest {

    private AccountService accountService;

    @Test
    public void testQueryAll() throws SQLException {
        List<Account> accounts = accountService.queryAll();
        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    @Test
    public void testFindById() throws SQLException {
        Account account = accountService.findById(4);
        System.out.println(account);
    }

    @Test
    public void testSave() throws SQLException {
        Account account = new Account();
        account.setName("rose");
        account.setMoney(100f);

        accountService.save(account);
    }

    @Test
    public void testEdit() throws SQLException {
        Account account = accountService.findById(4);
        account.setMoney(0f);
        accountService.edit(account);

    }

    @Test
    public void testDelete() throws SQLException {
        accountService.delete(4);

    }

    @Before
    public void init(){
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        accountService = app.getBean("accountService", AccountService.class);
    }
}
