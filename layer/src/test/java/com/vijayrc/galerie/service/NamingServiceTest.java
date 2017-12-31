package com.vijayrc.galerie.service;

import org.junit.Test;

import java.io.File;

public class NamingServiceTest {
    private NamingService service = new NamingService();

    @Test
    public void shouldShortenNames(){
        File file = new File("/home/vijayrc/study/study9b");
        service.shortenNames(file);
    }
}
