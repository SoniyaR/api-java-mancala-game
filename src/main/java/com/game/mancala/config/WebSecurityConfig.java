package com.game.mancala.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebSecurityConfig implements WebMvcConfigurer{
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept")
                .allowCredentials(true);
    }

//    @Bean
//    public WebMvcConfigurer corsMappingConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE")//, "OPTIONS", "HEAD")
//                        .maxAge(3600)
//                        .allowedHeaders("Origin, X-Requested-With, Content-Type, Accept")
////                        .exposedHeaders("X-Get-Header")
//                        .allowedOrigins("*");
////                        .allowedOrigins("http://localhost:3000/");
//            }
//        };
//    }
}
