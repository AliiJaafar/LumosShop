package com.dgpad.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        /*String dirName = "user-photos";
        Path userPhotoDirectory = Paths.get(dirName);

        String userPhotoPath = userPhotoDirectory.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + dirName + "/**")
                .addResourceLocations("file:/" + userPhotoPath + "/");
*/

        String categoryImageDirName = "category-images";
        Path categoryDirectory = Paths.get(categoryImageDirName);

        String categoryPath = categoryDirectory.toFile().getAbsolutePath();

        registry.addResourceHandler( categoryImageDirName + "/**")
                .addResourceLocations("file:/" + categoryPath + "/");

        registerResourceHandler(registry, "user-photos");
//        registerResourceHandler(registry, "category-images");
        registerResourceHandler(registry, "product-images");
        registerResourceHandler(registry, "logo-image");

    }

    private void registerResourceHandler(ResourceHandlerRegistry registry, String dirName) {
        Path directory = Paths.get(dirName);
        String path = directory.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + dirName + "/**")
                .addResourceLocations("file:/" + path + "/");
    }

}
