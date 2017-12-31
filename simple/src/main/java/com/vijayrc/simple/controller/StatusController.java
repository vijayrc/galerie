package com.vijayrc.simple.controller;

import com.vijayrc.simple.meta.WebClass;
import com.vijayrc.simple.meta.WebMethod;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@WebClass("status")
public class StatusController extends BaseController {
    @WebMethod
    public void showHome(Request request, Response response) throws Exception {
        htmlContent(response);
        Map<String, Object> model = new HashMap<>();
        myView.render("status", model, response);
    }

}
