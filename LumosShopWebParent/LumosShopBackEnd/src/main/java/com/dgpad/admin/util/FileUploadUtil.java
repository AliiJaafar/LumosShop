package com.dgpad.admin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

    public static void saveFile(String uploadDirectory, String fileName, MultipartFile multipartFile) throws IOException {

        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);

        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new IOException("Could not save file: " + fileName, e);
        }
    }

    public static void cleanDir(String dir) {
        Path dirPath = Paths.get(dir);

        try {
            Files.list(dirPath).forEach(file -> {
                if (!Files.isDirectory(file)) {
                    try {
                        Files.delete(file);

                    } catch (IOException e) {
                        LOGGER.error("Could Not Delete file: " + file);
                    }
                }
            });
        } catch (IOException e) {
            LOGGER.error("Could Not List Directory : " + dirPath);
        }
    }

    public static void removeDir(String dir) {

        LOGGER.info("FileUploadUtil | removeDir is started");

        LOGGER.info("FileUploadUtil | removeDir | dir : " + dir);

        cleanDir(dir);

        LOGGER.info("FileUploadUtil | cleanDir(dir) is over");

        try {
            Files.delete(Paths.get(dir));
        } catch (IOException e) {
            LOGGER.error("Could not remove directory: " + dir);
        }

    }
}
