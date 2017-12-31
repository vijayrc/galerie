package com.vijayrc.galerie.service;

import org.apache.log4j.Logger;

import java.io.File;

public abstract class FileService {
    private static Logger log  = Logger.getLogger(FileService.class);

    protected boolean isInvalidFolder(File dir){
        if (dir.exists() || dir.isDirectory()) return false;
        log.info(dir.getName()+"| does not exist");
        return true;
    }
    protected boolean isEmpty(File dir){
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) return false;
        log.info(dir.getName()+"| empty folder");
        return true;
    }
}
