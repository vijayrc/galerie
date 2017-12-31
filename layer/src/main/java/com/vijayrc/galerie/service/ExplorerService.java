package com.vijayrc.galerie.service;

import com.vijayrc.galerie.domain.MyFile;
import com.vijayrc.galerie.domain.MyFileList;
import com.vijayrc.galerie.domain.MyFolder;
import com.vijayrc.galerie.domain.MyThumbs;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

@Service
public class ExplorerService extends FileService implements ThumbsService {
    private static Logger log  = Logger.getLogger(ExplorerService.class);

    @Override
    public void run(MyThumbs myThumbs) throws Exception {
        myThumbs.withFileList(listFiles(myThumbs.file()));
    }

    public MyFileList listFiles(File dir) throws Exception {
        List<MyFile> myFiles = new ArrayList<>();
        if(isInvalidFolder(dir) || isEmpty(dir)) return new MyFileList(myFiles);
        File[] files = dir.listFiles();
        asList(files).stream()
                .filter(f -> !f.isDirectory() && !f.getName().matches("(?i)(Thumbs.db|picasa.ini)"))
                .forEach(f -> myFiles.add(new MyFile(f.getName(), f.getAbsolutePath(), f.length())));

        log.info("list:"+ files.length);
        return new MyFileList(myFiles);
    }

    public List<MyFolder> listFirstLevelFolders(String folder){
        List<MyFolder> folders = new ArrayList<>();
        File dir = new File(folder);
        if(isInvalidFolder(dir) || isEmpty(dir)) return folders;
        asList(dir.listFiles()).stream()
                .filter(f -> f.isDirectory() && !f.getName().contains(".galerie"))
                .forEach(f -> folders.add(new MyFolder(f)));
        Collections.sort(folders);
        log.info("list: "+folder+"|"+folders.size());
        return folders;
    }

    public List<String> listAllNestedFolders(String folder) {
        List<String> paths = new ArrayList<>();
        File dir = new File(folder);
        if(isInvalidFolder(dir) || isEmpty(dir)) return paths;
        recurse(dir, paths);
        log.info("list: "+paths.size());
        return paths;

    }

    public boolean open(String path) {
        try {
            Desktop.getDesktop().open(new File(path));
        } catch (Exception e) {
            log.error(e);
            return false;
        }
        return true;
    }

    private void recurse(File dir, List<String> paths){
        for (File file : dir.listFiles()) {
            if(!file.isDirectory()) continue;
            paths.add(file.getAbsolutePath());
            recurse(file, paths);
        }
    }
}
