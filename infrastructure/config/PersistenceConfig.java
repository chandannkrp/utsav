package dev.utsav.infrastructure;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EntityScan(basePackages = "dev.utsav.infrastructure")
@EnableJpaRepositories(basePackages = "dev.utsav.infrastructure")
public class PersistenceConfig {
    
}
