package com.ustc.ruoan.framework.web.configuration;

import com.ustc.ruoan.framework.web.util.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author ruoan
 */
@Configuration
@PropertySource(value = "classpath:ruoan.properties")
public class WebConfiguration {

    @Bean
    public Person person() {
        return new Person();
    }
}
