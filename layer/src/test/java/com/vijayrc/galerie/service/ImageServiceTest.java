package com.vijayrc.galerie.service;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageServiceTest {
    @Test
    public void splitAGif() throws IOException {
        ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
        ImageInputStream in = ImageIO.createImageInputStream(new File("/C:/Users/vchakrav/Pictures/eyeaz/1.gif"));
        reader.setInput(in);
        for (int i = 0, count = reader.getNumImages(true); i < count; i++) {
            BufferedImage image = reader.read(i);
            String split = "split" + i + ".png";
            ImageIO.write(image, "PNG", new File(split));
            System.out.println(split);
        }
    }
    @Test
    public void scaleImage() throws IOException {
        File inputFile = new File("some file path");
        BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * 0.5);
        int scaledHeight = (int) (inputImage.getHeight() * 0.5);
        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        OutputStream out = new FileOutputStream(new File("scaled File"));
        ImageIO.write(outputImage, "JPG", out);
    }
    @Test
    public void makeThumbs() throws IOException {
        String path = "/home/vijayrc/pics/wallpapers";
        File srcDir = new File(path);
        File targetDir = new File(path+"/.galerie");
        if(!targetDir.exists())targetDir.mkdir();

        FileFilter filter = f -> !f.isDirectory() && notIn(targetDir, f) && isImage(f);
        File[] files = srcDir.listFiles(filter);
        if(files.length ==0){
            System.out.println("thumbs already generated");
            return;
        }
        Thumbnails.of(files).width(300).outputFormat("jpg").toFiles(targetDir,Rename.SUFFIX_HYPHEN_THUMBNAIL);
    }

    private boolean notIn(File dir, File file) {
        String name = removeExt(file.getName())+"-thumbnail.jpg";
        return dir.list((d, n) -> n.equals(name)).length == 0;
    }
    private String removeExt(String name) {
        return name.substring(0, name.lastIndexOf('.'));
    }
    private boolean isImage(File file){
        return file.getName().matches("(.*).(?i)(jpg|png|gif|jpeg)");
    }
}
