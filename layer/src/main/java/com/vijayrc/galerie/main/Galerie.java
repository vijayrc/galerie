package com.vijayrc.galerie.main;

import com.vijayrc.simple.main.Simple;

import java.awt.*;
import java.net.URI;

public class Galerie {
    public static void main(String[] args) throws Exception {
        Simple.run();
        Desktop.getDesktop().browse(URI.create("http://localhost:9091/galerie/home"));
    }
}
