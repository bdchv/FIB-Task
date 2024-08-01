package com.project.conf;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
public class Config {
    @Value("${app.username}")
    String username;

    @Value("${app.apiKey}")
    String apiKey;

}
