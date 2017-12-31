package com.vijayrc.simple.controller;

import com.vijayrc.simple.meta.WebClass;
import com.vijayrc.simple.meta.WebMethod;
import com.vijayrc.simple.web.MyServer;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@WebClass("stop")
@Scope("singleton")
public class StopController extends BaseController {
    @Autowired
    private MyServer myServer;

    @WebMethod
    public void stop(Request request, Response response) throws Exception {
        htmlContent(response);
        Map<String, Object> model = new HashMap<>();
        myView.render("stop", model, response);
        myServer.stop();
    }

}
