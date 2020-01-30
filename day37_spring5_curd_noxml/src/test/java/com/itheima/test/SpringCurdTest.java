package com.itheima.test;

import com.itheima.config.AppConfig;
import com.itheima.domain.Account;
import com.itheima.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class SpringCurdTest {

    @Autowired
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
}
