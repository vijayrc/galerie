package com.vijayrc.galerie.domain;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class MyFile implements Comparable<MyFile>{
    private String name;
    private String path;
    private String type;
    private long size;
    private List<MyTag> tags = new ArrayList<>();

    public MyFile(String name, String path, long size) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.type = getTypeFromName(name);
    }
    public String thumbPath() {
        if(name.matches("(.*).(?i)(gif)"))
            return "pic/"+path;
        else if (name.matches("(.*).(?i)(png)"))
            return "pic/"+ removeNameFromPath(path)+".galerie/"+ removeTypeFromName(name) + "-thumbnail.png.jpg";
        else if (name.matches("(.*).(?i)(jpg|jpeg)"))
            return "pic/"+ removeNameFromPath(path)+".galerie/"+ removeTypeFromName(name) + "-thumbnail.jpg";
        else
            return "static/images/file.png";
    }
    public String name() {return name;}
    public String type() {return type;}
    public String origPath() {return isImage()? "pic/"+path: path;}
    public String path() {return path;}
    public String description() {return name+" | "+size();}
    public String size(){return size/(1024*1024)+" MB";}
    public String longSize() {return String.valueOf(size);}
    public boolean isNotImage(){return !isImage();}
    public boolean isImage(){return name.matches("(.*).(?i)(gif|jpg|png|jpeg)");}

    @Override
    public int compareTo(MyFile o) {
        return this.name.compareToIgnoreCase(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyFile myFile = (MyFile) o;
        return !(path != null ? !path.equals(myFile.path) : myFile.path != null);
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }

    public MyFile withTags(String tagStr){
        asList(tagStr.split(",")).forEach(t -> tags.add(new MyTag(t)));
        return this;
    }

    public String tags() {
        StringBuilder b = new StringBuilder();
        tags.forEach(t -> b.append(t).append(","));
        return b.toString();
    }

    private String getTypeFromName(String name) {return name.contains(".")? name.substring(name.lastIndexOf('.')):"";}
    private String removeTypeFromName(String name) {return name.substring(0,name.lastIndexOf('.'));}
    private String removeNameFromPath(String path){return path.substring(0,path.indexOf(name));}

}
