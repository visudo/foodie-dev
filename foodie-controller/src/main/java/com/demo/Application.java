package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.demo.mapper")
@ComponentScan(basePackages = {"com.demo","org.n3r.idworker"})
public class Application /**extends SpringBootServletInitializer**/ {

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(Application.class);
//    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
