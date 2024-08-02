package com.project.conf;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Value("${username}")
    String username;

    @Value("${apiKey}")
    String apiKey;

}
