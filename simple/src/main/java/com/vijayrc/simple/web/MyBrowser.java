package com.vijayrc.simple.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.net.URI;

@Service
public class MyBrowser {
    private Integer port;

    @Autowired
    public MyBrowser(@Value("#{config['server.port']}") Integer port) {
        this.port = port;
    }
    public void start() throws Exception {
        Desktop.getDesktop().browse(URI.create("http://localhost:" + port));
    }
}
