package com.freedomfm.singer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "email-template")
public class EmailTemplateProperties {
    private HashMap<String, String> links;
    private HashMap<String, String> logos;
}
