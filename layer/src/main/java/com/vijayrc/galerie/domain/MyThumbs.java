package com.vijayrc.galerie.domain;

import java.io.File;

public class MyThumbs {
    private File file;
    private MyFileList myFileList;

    public File file() {return file;}
    public MyFileList myFileList() {return myFileList;}

    public MyThumbs withFile(String path) {
        this.file = new File(path.trim());
        return this;
    }
    public MyThumbs withFileList(MyFileList myFileList) {
        this.myFileList = myFileList;
        return this;
    }
}
