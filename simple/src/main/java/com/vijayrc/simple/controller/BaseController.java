package com.vijayrc.simple.controller;

import com.vijayrc.simple.web.MyView;
import org.simpleframework.http.Response;

public abstract class BaseController{
    protected MyView myView = new MyView();

    protected void htmlContent(Response response) {
        response.setValue("Content-Type", "text/html");
    }
}
