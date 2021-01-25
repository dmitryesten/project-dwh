package com.sample.leantech.transfer.config;

import org.apache.spark.internal.config.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class JiraRestConfig {

    @Value("${jira.hostname}")
    private String jiraHost;
    @Value("${jira.port}")
    private String jiraPort;

    @Value("${jira.nickname}")
    private String jiraNickname;
    @Value("${jira.password}")
    private String jiraPassword;

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://"+jiraHost+":"+jiraPort));
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(jiraNickname, jiraPassword));
        return restTemplate;
    }

}
