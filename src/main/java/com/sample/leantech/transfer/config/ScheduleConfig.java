package com.sample.leantech.transfer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
// TODO: uncomment when the application is ready
//@EnableScheduling
@Profile("!tests")
public class ScheduleConfig {
}