package com.vijayrc.galerie.domain;

import org.junit.Test;

public class MyFileTest {
    @Test
    public void testThumbPath() throws Exception {
        MyFile myFile1 = new MyFile("rose.jpg","/home/vijayrc/pics/wallpapers/rose.jpg",123);
        MyFile myFile2 = new MyFile("rose.gif","/home/vijayrc/pics/wallpapers/rose.gif",123);
        MyFile myFile3 = new MyFile("ftop.ru_38144.jpg","/home/vijayrc/pics/wallpapers/ftop.ru_38144.jpg",123);

        System.out.println(myFile1.thumbPath());
        System.out.println(myFile2.thumbPath());
        System.out.println(myFile3.thumbPath());
        System.out.println(myFile1.type());
    }
}
