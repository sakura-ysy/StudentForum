package com.example.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@MapperScan("com.example.backend.module.*.mapper")
@SpringBootApplication()
public class DoubaoTestApplication extends SpringBootServletInitializer {

    @Override  // 重写 configure 固定格式
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DoubaoTestApplication.class) ;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DoubaoTestApplication.class, args);
        // 打印容器中的所有组件
        String[] beanDefinitionNames = run.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            // System.out.println(beanDefinitionName);
        }
    }

}
