package dev.utsav.api;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@SpringBootApplication(scanBasePackages = "dev.utsav")
@ConfigurationPropertiesScan
public class UtsavApplication{
    public static void main(String[] args){
        SpringApplication.run(UtsavApplication.class);
    }
}