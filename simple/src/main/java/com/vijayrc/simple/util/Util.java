package com.vijayrc.simple.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Util {
    @Value("#{config['packaged']}")
    private String packaged;

    public String resource(String fileName) {
        return packaged.equalsIgnoreCase("true")? userDir() + fileName: ClassLoader.getSystemResource(fileName).getFile();
    }
    public static String userDir() {
        return System.getProperty("user.dir") + System.getProperty("file.separator");
    }
}
