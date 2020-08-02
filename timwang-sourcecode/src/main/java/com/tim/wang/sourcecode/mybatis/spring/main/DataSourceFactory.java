package com.tim.wang.sourcecode.mybatis.spring.main;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author wangjun
 * @date 2020-08-01
 */
public class DataSourceFactory {

    public static DataSource getDataSource() {
        PooledDataSource pooledDataSource = new PooledDataSource();
        pooledDataSource.setDriver("com.mysql.cj.jdb");
        pooledDataSource.setUrl("jdbc:mysql://localhost:3306/zp");
        pooledDataSource.setUsername("root");
        pooledDataSource.setPassword("123456");
        return pooledDataSource;
    }

}
