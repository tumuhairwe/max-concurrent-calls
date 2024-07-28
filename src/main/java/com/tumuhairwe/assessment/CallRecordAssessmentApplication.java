package com.tumuhairwe.assessment;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CallRecordAssessmentApplication {
    
    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(CallRecordAssessmentApplication.class, args);
    }
}
