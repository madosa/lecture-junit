package com.interpark.lecture.junit.common.config;

import com.interpark.ncl.std.common.dao.CommonMaxValueIncrementer;
import com.interpark.ncl.std.common.datasource.DataSourceRouter;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan(basePackages = {"com.interpark.lecture.junit"})
public class DataSourceConfig {

    @Bean
    public CommonMaxValueIncrementer imageSequence(@Autowired @Qualifier("dataSource") DataSource dataSource) {

        CommonMaxValueIncrementer commonMaxValueIncrementer = new CommonMaxValueIncrementer();
        commonMaxValueIncrementer.setIncrementerName("seq_image");
        commonMaxValueIncrementer.setColumnName("seq");
        commonMaxValueIncrementer.setDataSource(dataSource);

        return commonMaxValueIncrementer;
    }

    @Bean
    public DataSourceRouter dataSourceRouter(@Autowired @Qualifier("dataSource") DataSource dataSource) {

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("MYSQL", dataSource);

        DataSourceRouter dataSourceRouter = new DataSourceRouter();
        dataSourceRouter.setTargetDataSources(targetDataSources);
        dataSourceRouter.setDefaultTargetDataSource(dataSource);

        return dataSourceRouter;
    }

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix="spring.datasource.hikari")
    public DataSource dataSource(){
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Autowired @Qualifier("dataSource") DataSource dataSource, ApplicationContext applicationContext) throws Exception {

        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setConfigLocation(applicationContext.getResource("classpath:sql/sqlSession-config.xml"));

        return sessionFactory.getObject();
    }

    @Primary
    @Bean(name = "sqlSession")
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager(@Autowired @Qualifier("dataSource") DataSource dataSource) {

        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);

        return transactionManager;
    }
}
