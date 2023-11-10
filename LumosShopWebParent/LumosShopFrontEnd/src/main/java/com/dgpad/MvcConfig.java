package com.dgpad;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final List<String> resourceDirectories = Arrays.asList("user-photos", "category-images", "product-images");

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        for (String dirName : resourceDirectories) {
            registerResourceHandler(registry, dirName);
        }
    }

    private void registerResourceHandler(ResourceHandlerRegistry registry, String dirName) {
        Path directory = Paths.get(dirName);
        String path = directory.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + dirName + "/**")
                .addResourceLocations("file:/" + path + "/");
    }
}
