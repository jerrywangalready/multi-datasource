package com.example.multidatasource.config;

import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;


import javax.sql.DataSource;


@Configuration
@MapperScan(basePackages = "com.example.multidatasource.demo.mapper.hive", sqlSessionFactoryRef = "db2SqlSessionFactory")
@Log4j2
public class HiveConfig {

    @Primary
    @Bean("db2DataSource")
    @ConfigurationProperties("spring.datasource.druid.hive")
    public DataSource getDb2DataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Primary
    @Bean("db2SqlSessionFactory")
    public SqlSessionFactory db2SqlSessionFactory(@Qualifier("db2DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // mapper的xml形式文件位置必须要配置，不然将报错：no statement （这种错误也可能是mapper的xml中，namespace与项目的路径不一致导致）
        // 设置mapper.xml路径，classpath不能有空格
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/example/multidatasource/demo/mapper/hive/*.xml"));
        return bean.getObject();
    }

    @Primary
    @Bean("db2SqlSessionTemplate")
    public SqlSessionTemplate db2SqlSessionTemplate(@Qualifier("db2SqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
