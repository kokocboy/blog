package com.example.test;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import threadPool.ThreadPool;

@SpringBootApplication
public class TestApplication {
    public static void main(String[] args) {
        ThreadPool threadPool=ThreadPool.instance();
        SpringApplication.run(TestApplication.class, args);
    }

}
