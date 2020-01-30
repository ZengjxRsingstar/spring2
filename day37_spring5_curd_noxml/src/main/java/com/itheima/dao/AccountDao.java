package com.itheima.dao;

import com.itheima.domain.Account;

import java.sql.SQLException;
import java.util.List;

public interface AccountDao {
    List<Account> queryAll() throws SQLException;

    Account findById(Integer id) throws SQLException;

    void save(Account account) throws SQLException;

    void edit(Account account) throws SQLException;

    void delete(Integer id) throws SQLException;
}
