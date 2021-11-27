package com.fastcampus.housebatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.util.Assert;
@EnableBatchProcessing
@EnableJpaAuditing
@SpringBootApplication
public class HouseBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(HouseBatchApplication.class, args);
    }

}
