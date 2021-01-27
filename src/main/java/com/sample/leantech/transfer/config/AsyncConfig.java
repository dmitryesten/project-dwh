package com.sample.leantech.transfer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
// TODO: uncomment when the application is ready
//@EnableAsync
@Profile("!tests")
public class AsyncConfig {
}