package com.itheima.dao.impl;

import com.itheima.dao.AccountDao;
import com.itheima.domain.Account;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class AccountDaoImpl implements AccountDao {

    private QueryRunner runner;

    public void setRunner(QueryRunner runner) {
        this.runner = runner;
    }

    @Override
    public List<Account> queryAll() throws SQLException {
        return runner.query("select * from account", new BeanListHandler<>(Account.class));
    }

    @Override
    public Account findById(Integer id) throws SQLException {
        return runner.query("select * from account where id = ?", new BeanHandler<>(Account.class), id);
    }

    @Override
    public void save(Account account) throws SQLException {
        runner.update("insert into account (id,name,money) values(?,?,?)", account.getId(), account.getName(), account.getMoney());
    }

    @Override
    public void edit(Account account) throws SQLException {
        runner.update("update account set name = ?, money = ? where id = ?", account.getName(), account.getMoney(), account.getId());
    }

    @Override
    public void delete(Integer id) throws SQLException {
        runner.update("delete from account where id = ?", id);
    }
}
