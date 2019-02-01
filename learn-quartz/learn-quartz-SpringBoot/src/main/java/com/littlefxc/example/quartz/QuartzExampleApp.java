package com.littlefxc.example.quartz;

import com.littlefxc.example.quartz.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

/**
 * 学习spring-boot-starter-quartz
 *
 * @author fengxuechao
 * @date 12/18/2018
 **/
@Slf4j
@SpringBootApplication
public class QuartzExampleApp {


    public static void main(String[] args) {
        SpringApplication.run(QuartzExampleApp.class, args);
    }

//    @Component
    public static class StartUpListener implements ApplicationRunner {

        @Autowired
        private SchedulerService schedulerService;

        @Override
        public void run(ApplicationArguments args) throws Exception {
            log.info("Schedule all new scheduler jobs at app startup - starting");
            try {
                schedulerService.startAllSchedulers();
                log.info("Schedule all new scheduler jobs at app startup - complete");
            } catch (Exception ex) {
                log.error("Schedule all new scheduler jobs at app startup - error", ex);
            }
        }
    }
}
