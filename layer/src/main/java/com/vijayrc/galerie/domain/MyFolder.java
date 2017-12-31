package com.vijayrc.galerie.domain;

import java.io.File;

public class MyFolder implements Comparable<MyFolder>{
    private String name;
    private String path;

    public MyFolder(File file) {
        name = file.getName();
        path = file.getAbsolutePath();
    }
    public String name() {return name;}
    public String path() {return path;}

    @Override
    public int compareTo(MyFolder o) {
        return name.compareTo(o.name);
    }
}

