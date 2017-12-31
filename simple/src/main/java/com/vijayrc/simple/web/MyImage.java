package com.vijayrc.simple.web;

public enum MyImage {
    gif("image/gif"),png("image/png"),jpg("image/jpg"),jpeg("image/jpg");

    private String contentType;

    MyImage(String contentType) {
        this.contentType = contentType;
    }
    public static String contentType(String path) {
        for (MyImage myImage : MyImage.values())
            if(path.matches("(.*).(?i)"+myImage.name())) return myImage.contentType;
        return jpg.contentType;
    }
    public static MyImage getFor(String path) {
        for (MyImage myImage : MyImage.values())
            if(path.matches("(.*).(?i)"+myImage.name())) return myImage;
        return jpg;
    }
    public String contentType() {
        return contentType;
    }
    public boolean isGif() {
        return gif.equals(this);
    }
}
