package com.example.kuromi;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@MapperScan(basePackages = "com.example.kuromi.mapper")
@SpringBootApplication
public class KuromiApplication {

    public static void main(String[] args) {
        SpringApplication.run(KuromiApplication.class, args);
    }

}
