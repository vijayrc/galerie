package com.vijayrc.galerie.service;

import com.vijayrc.galerie.domain.MyThumbs;
import com.vijayrc.simple.util.Util;
import com.vijayrc.simple.web.MyImage;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.substringAfter;

@Service
public class ImageService extends FileService implements ThumbsService {
    private static Logger log = Logger.getLogger(ImageService.class);

    @Override
    public void run(MyThumbs myThumbs) throws Exception {
        makeThumbs(myThumbs.file());
    }

    public void showPic(Request request, Response response) throws Exception {
        String path = substringAfter(request.getPath().toString(), "pic");
        File file = new File(path);
        OutputStream out = response.getOutputStream();
        try {
            if (file.exists() && !file.isDirectory()) {
                MyImage myImage = MyImage.getFor(path);
                InputStream is = new FileInputStream(file);
                byte[] buf = new byte[64 * 1024];
                int nRead;
                while( (nRead=is.read(buf)) != -1 )
                    out.write(buf, 0, nRead);
                response.setValue("Content-Type", myImage.contentType());
            } else {
                log.error("missing:"+path);
                showError(out);
            }
        } catch (Exception e) {
            log.error(e);
            showError(out);
        }
        out.flush();
        out.close();
    }

    public void makeThumbs(File srcDir) throws Exception {
        if(isInvalidFolder(srcDir) || isEmpty(srcDir)) return;
        final File targetDir = new File(srcDir.getAbsolutePath()+ "/.galerie");
        if(!targetDir.exists()) targetDir.mkdir();

        FileFilter filter = f -> !f.isDirectory() && isImage(f) && notIn(targetDir, f);
        File[] files = srcDir.listFiles(filter);
        if (files.length == 0)
            log.info("thumbnails already generated");
        else{
            makeThumbs(targetDir, files);
            renameJpegs(targetDir);
        }
    }

    private void makeThumbs(File targetDir, File[] files) throws Exception {
        log.info("thumbs: start, please wait..");
        final CountDownLatch latch = new CountDownLatch(files.length);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        try {
            asList(files).stream().forEach(f -> executor.execute(
                    () -> {
                        try {
                            Thumbnails.of(f)
                                    .width(200)
                                    .outputFormat("jpg")
                                    .toFiles(targetDir, Rename.SUFFIX_HYPHEN_THUMBNAIL);
                            log.info("thumb: "+f.getName());
                            latch.countDown();
                        } catch (Exception e) {
                           log.error(e);
                        }
                    }));
            latch.await();
        } finally {
            executor.shutdown();
        }
        log.info("thumbs: end");
    }

    private void showError(OutputStream out) throws IOException {
        out.write("".getBytes());
    }

    private void renameJpegs(File targetDir) throws IOException {
        File[] files = targetDir.listFiles(f -> f.getName().contains(".jpeg") || f.getName().contains(".JPG"));
        for (File file : files) {
            File destFile = new File(targetDir + "/" + removeExt(file.getName()) + ".jpg");
            if(destFile.exists()) continue;
            FileUtils.moveFile(file, destFile);
        }
    }
    private boolean notIn(File dir, File file) {
        String name = file.getName();
        String title = removeExt(name);
        String thumbName = name.contains(".png")?
                title.replace(".png","")+"-thumbnail.png.jpg":
                title+"-thumbnail.jpg";
        return dir.list((d, n) -> n.equalsIgnoreCase(thumbName)).length == 0;
    }
    private String removeExt(String name) {
        return name.substring(0, name.lastIndexOf('.'));
    }
    private boolean isImage(File file){
        return file.getName().matches("(.*).(?i)(jpg|png|jpeg)");
    }
}
