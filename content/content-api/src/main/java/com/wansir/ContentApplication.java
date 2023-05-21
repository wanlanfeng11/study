package com.wansir;


import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableSwagger2Doc
public class ContentApplication {

    @Value("swagger.title")
    String s;

    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
    }
}