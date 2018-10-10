package com.zyc.jobmanager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/**
 * @Author: Richard Zeng
 * @Description:
 * @Date: Created in 18:25 2018\10\10
 */
/*@ComponentScan("com.example.demo")*/
@MapperScan("com.zyc.jobmanager.dao")
public class JobManagerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(JobManagerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("欢迎来参观Job Manager");
    }

}
