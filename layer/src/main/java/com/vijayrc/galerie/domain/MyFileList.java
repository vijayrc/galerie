package com.vijayrc.galerie.domain;

import java.util.*;

import static java.util.Collections.sort;

public class MyFileList {
    private Set<MyFile> myFiles;
    private List<MyFile> imgFiles = new ArrayList<>();
    private List<MyFile> mediaFiles = new ArrayList<>();

    public MyFileList(List<MyFile> list) {
        this.myFiles = new LinkedHashSet<>(list);
        myFiles.stream().filter(MyFile::isImage).forEach(imgFiles::add);
        myFiles.stream().filter(MyFile::isNotImage).forEach(mediaFiles::add);
        sort(imgFiles);
        sort(mediaFiles);
    }
    public Map<String,Set<MyFile>> groupByExt(){
        Map<String,Set<MyFile>> map = new TreeMap<>();
        Map<String,Integer> keys = new HashMap<>();

        mediaFiles.forEach(f -> {
            String type = f.type();
            if (!keys.containsKey(type)) {
                keys.put(type,1);
                map.put(type+1, new TreeSet<>());
            }
            Integer batch = keys.get(type);
            Set<MyFile> files = map.get(type + batch);
            if(files.size() > 20){
                int newBatch = batch + 1;
                keys.put(type, newBatch);
                TreeSet<MyFile> newFiles = new TreeSet<>();
                newFiles.add(f);
                map.put(type+newBatch, newFiles);
            }else{
                files.add(f);
            }
        });
        return map;
    }

    public List<MyFile> imgFiles() {return imgFiles;}
    public Set<MyFile> all() {return myFiles;}
    public boolean isEmpty() {return myFiles.isEmpty();}
    public boolean hasNoMediaFiles() {return mediaFiles.isEmpty();}
    public int mediaCount() {return mediaFiles.size();}
}
