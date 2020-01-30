package com.itheima.config;

import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

public class JdbcConfig {
    @Bean("runner")
    public QueryRunner queryRunner(@Qualifier("ds") DataSource dataSource){
        return new QueryRunner(dataSource);
    }
}
