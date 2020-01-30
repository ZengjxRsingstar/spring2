package com.itheima.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
@ComponentScan(basePackages = "com.itheima")
@PropertySource("classpath:jdbc.properties")
@Import({JdbcConfig.class})
public class AppConfig {

    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    //声明一个连接池对象
    @Bean("ds")
    public DataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(driver);
        dataSource.setJdbcUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    /*@Bean("runner")
    public QueryRunner queryRunner() throws PropertyVetoException {
        //如果 依赖的方法，就在当前类里，可以直接调用方法得到依赖的值
        return new QueryRunner(dataSource());
    }*/

    /*
        使用@Bean注解，配置到一个方法上，
            方法返回值被声明成为一个bean对象
            方法的参数，就是bean对象的依赖。
                Spring会自动注入，byType注入， 相当于@Autowired
                @Qualifier注解加在方法参数上，相当于@Autowired+@Qualifier， byName注入
     */
    /*@Bean("runner")
    public QueryRunner queryRunner(@Qualifier("ds") DataSource dataSource){
        return new QueryRunner(dataSource);
    }

    public void show(){
        System.out.println(this.driver);
        System.out.println(this.url);
        System.out.println(this.username);
        System.out.println(this.password);
    }*/
}
