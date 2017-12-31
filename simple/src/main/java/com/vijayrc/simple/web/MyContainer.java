package com.vijayrc.simple.web;

import com.vijayrc.simple.controller.AllControllers;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;

@Service
@Scope("singleton")
public class MyContainer implements Container {
    private static Logger log = Logger.getLogger(MyContainer.class);
    private Executor executor;
    private AllControllers allControllers;

    @Autowired
    public MyContainer(AllControllers allControllers) {
        this.allControllers = allControllers;
        this.executor = Executors.newCachedThreadPool();
    }
    @Override
    public void handle(Request request, Response response) {
        executor.execute(() -> {
            try {
                allControllers.act(request, response);
            } catch (Exception e) {
                log.error(request.getPath(), e);
            }
        });
    }

}
