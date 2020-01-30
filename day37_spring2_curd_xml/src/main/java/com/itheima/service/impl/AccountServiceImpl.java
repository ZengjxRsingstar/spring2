package com.itheima.service.impl;

import com.itheima.dao.AccountDao;
import com.itheima.domain.Account;
import com.itheima.service.AccountService;

import java.sql.SQLException;
import java.util.List;

public class AccountServiceImpl implements AccountService {
    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public List<Account> queryAll() throws SQLException {
        return accountDao.queryAll();
    }

    @Override
    public Account findById(Integer id) throws SQLException {
        return accountDao.findById(id);
    }

    @Override
    public void save(Account account) throws SQLException {
        accountDao.save(account);
    }

    @Override
    public void edit(Account account) throws SQLException {
        accountDao.edit(account);
    }

    @Override
    public void delete(Integer id) throws SQLException {
        accountDao.delete(id);
    }
}
