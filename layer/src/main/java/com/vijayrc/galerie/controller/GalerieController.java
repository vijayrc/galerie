package com.vijayrc.galerie.controller;

import com.vijayrc.galerie.domain.MyFileList;
import com.vijayrc.galerie.domain.MyFolder;
import com.vijayrc.galerie.process.ThumbsProcess;
import com.vijayrc.galerie.service.*;
import com.vijayrc.galerie.domain.MyThumbs;
import com.vijayrc.simple.controller.BaseController;
import com.vijayrc.simple.meta.WebClass;
import com.vijayrc.simple.meta.WebMethod;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@WebClass("galerie")
public class GalerieController extends BaseController {
    @Autowired
    private ImageService imageService;
    @Autowired
    private IndexService indexService;
    @Autowired
    private NamingService namingService;
    @Autowired
    private ThumbsProcess thumbsProcess;
    @Autowired
    private ExplorerService explorerService;

    @WebMethod("home")
    public void show(Request request, Response response) throws Exception {
        htmlContent(response);
        Map<String, Object> model = new HashMap<>();
        myView.render("galerie", model, response);
    }

    @WebMethod("thumbs")
    public void thumbs(Request request, Response response) throws Exception {
        htmlContent(response);
        String path = request.getParameter("root");
        MyThumbs myThumbs = thumbsProcess.execute(path);

        Map<String, Object> model = new HashMap<>();
        model.put("myFiles", myThumbs.myFileList());
        myView.render("thumbs", model, response);
    }

    @WebMethod("pic")
    public void showPic(Request request, Response response) throws Exception {
        imageService.showPic(request, response);
    }

    @WebMethod("play")
    public void play(Request request, Response response) throws Exception {
        htmlContent(response);
        String path = request.getParameter("path");
        boolean success = explorerService.open(path);

        Map<String, Object> model = new HashMap<>();
        model.put("path",path);
        model.put("success",success);
        myView.render("play", model, response);
    }

    @WebMethod("paths")
    public void paths(Request request, Response response) throws Exception {
        htmlContent(response);
        String path = request.getParameter("root");
        List<String> paths = explorerService.listAllNestedFolders(path);

        Map<String, Object> model = new HashMap<>();
        model.put("paths", paths);
        myView.render("paths", model, response);
    }

    @WebMethod("index")
    public void index(Request request, Response response) throws Exception {
        htmlContent(response);
        String path = request.getParameter("root");
        indexService.add(path);

        Map<String, Object> model = new HashMap<>();
        model.put("path",path);
        myView.render("index", model, response);
    }

    @WebMethod("search")
    public void search(Request request, Response response) throws Exception {
        htmlContent(response);
        String query = request.getParameter("root");
        MyFileList myFileList = indexService.search(query);

        Map<String, Object> model = new HashMap<>();
        model.put("myFiles", myFileList);
        myView.render("thumbs", model, response);
    }

    @WebMethod("tree")
    public void tree(Request request, Response response) throws Exception {
        htmlContent(response);
        String folder = request.getParameter("folder");
        List<MyFolder> myFolders = explorerService.listFirstLevelFolders(folder);

        Map<String, Object> model = new HashMap<>();
        model.put("myFolders", myFolders);
        model.put("parent", folder);
        myView.render("tree", model, response);
    }
}
