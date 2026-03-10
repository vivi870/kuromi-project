package com.example.kuromi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String AVATAR_ROOT_PATH = "file:D:/project/avatar/";

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                //允许前端域名
                .allowedOrigins("http://localhost:5174")
                //允许的请求方式
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                //允许的请求头
                .allowedHeaders("*")
                //是否允许携带Cookie（前后端认证时需要）
                .allowCredentials(true)
                //预检请求的有效期，避免频繁发送OPTIONS请求
                .maxAge(3600);
    }

    // 添加静态资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("classpath:/static/upload/");
        registry.addResourceHandler("/video/**")
                .addResourceLocations("classpath:/static/video/");
        registry.addResourceHandler("/avatar/**")
                .addResourceLocations(AVATAR_ROOT_PATH);
    }
}
