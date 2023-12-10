package com.vehicle.manager.vehicle.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {
    public static void saveInputStreamAsFile(InputStream inputStream, Path completeFilePath) throws IOException {
        Files.copy(inputStream, completeFilePath);
    }

    public static void createDirectoryIfNotExists(Path path) throws IOException {
        if (Files.notExists(path)) Files.createDirectory(path);
    }
}
