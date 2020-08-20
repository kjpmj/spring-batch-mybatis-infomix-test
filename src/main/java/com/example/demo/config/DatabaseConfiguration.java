package com.example.demo.config;

import com.informix.jdbcx.IfxDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.support.DatabaseType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class DatabaseConfiguration {

    private final DataSource dataSource;
    private final ApplicationContext applicationContext;

//    @Bean
    public DataSource batchDataSource() {
        IfxDataSource ifxDataSource = new IfxDataSource();
        ifxDataSource.setIfxIFXHOST("10.231.21.3");
        ifxDataSource.setServerName("testsvr");
        ifxDataSource.setDatabaseName("PGINFO");
        ifxDataSource.setPortNumber(3025);
        ifxDataSource.setUser("pgbase");
        ifxDataSource.setPassword("pgbase123");

        return ifxDataSource;
    }

    @Bean
    public BatchConfigurer batchConfig() {
        CustomBatchConfigurer configurer = new CustomBatchConfigurer(batchDataSource());
        return configurer;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(batchDataSource());
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/*.xml"));
        sqlSessionFactoryBean.setConfiguration(mybatisConfiguration());

        return sqlSessionFactoryBean.getObject();
    }

    private org.apache.ibatis.session.Configuration mybatisConfiguration () {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);

        return configuration;
    }
}
