package com.example.kuromi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String AVATAR_ROOT_PATH = "file:D:/project/avatar/";
    private static final String UPLOAD_ROOT_PATH = "file:D:/project/noteimg/";

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5174")
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // note 图片映射到本地 D:/project/upload/ 目录
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("classpath:/static/upload/");
        registry.addResourceHandler("/video/**")
                .addResourceLocations("classpath:/static/video/");
        registry.addResourceHandler("/avatar/**")
                .addResourceLocations(AVATAR_ROOT_PATH);
        registry.addResourceHandler("/noteimg/**")
                .addResourceLocations(UPLOAD_ROOT_PATH);
    }
}
