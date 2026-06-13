package com.J.M_CODERS.KinalCodeQuest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Garantiza que Spring MVC exponga correctamente la carpeta static para CSS/JS/Imágenes
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}