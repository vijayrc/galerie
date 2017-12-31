package com.vijayrc.simple.controller;

import com.vijayrc.simple.meta.WebClass;
import com.vijayrc.simple.meta.WebMethod;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.simpleframework.http.Response;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace;

@WebClass("error")
public class ErrorController extends BaseController {
    @WebMethod
    public void showError(Response response, Exception exception) throws Exception {
        String message = exception != null ? getFullStackTrace(exception) : "Error occurred. Please check the logs";
        Map<String, Object> model = new HashMap<>();
        model.put("error", message);
        myView.render("error", model, response);
    }
}
