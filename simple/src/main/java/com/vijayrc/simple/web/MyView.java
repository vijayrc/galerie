package com.vijayrc.simple.web;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.simpleframework.http.Response;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import static org.apache.velocity.app.VelocityEngine.*;

public class MyView {
    private VelocityEngine engine;

    public MyView() {
        engine = new VelocityEngine();
        engine.setProperty(RESOURCE_LOADER, "classpath");
        engine.setProperty("classpath." + RESOURCE_LOADER + ".class", ClasspathResourceLoader.class.getCanonicalName());
        engine.setProperty(RUNTIME_REFERENCES_STRICT, true);
        engine.init();
    }

    public void render(String viewName, Map<String, Object> model, Response response) throws IOException {
        VelocityContext context = new VelocityContext(model);
        Writer writer = new OutputStreamWriter(response.getPrintStream());
        engine.mergeTemplate("/views/" + viewName + ".vm", "UTF-8", context, writer);
        writer.close();
        response.close();
    }
}
