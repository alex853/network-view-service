package net.simforge.networkview.map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import javax.sql.DataSource;

@Configuration
public class AppConfig {
    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean localSessionFactory = new LocalSessionFactoryBean();
        localSessionFactory.setDataSource(dataSource);
        localSessionFactory.setPackagesToScan("net.simforge.networkview.core.report.persistence");
        return localSessionFactory;
    }
}
