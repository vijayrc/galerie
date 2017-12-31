package com.vijayrc.simple.controller;

import com.vijayrc.simple.meta.WebClass;
import com.vijayrc.simple.meta.WebMethod;
import com.vijayrc.simple.util.Util;
import com.vijayrc.simple.web.MyImage;
import org.apache.commons.io.FileUtils;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static org.apache.commons.lang.StringUtils.substringAfter;

@Component
@WebClass("static")
public class StaticController extends BaseController {
    @Autowired
    private Util util;

    @WebMethod
    public void show(Request request, Response response) throws Exception {
        String path = request.getPath().toString();
        String fileName = substringAfter(path, "static");
        String repo = util.resource("static");

        File file = new File(repo + fileName);
        if (!file.exists()) return;

        if (path.contains("images")) {
            MyImage myImage = MyImage.getFor(path);
            response.setValue("Content-Type", myImage.contentType());
            OutputStream out = response.getOutputStream();
            ImageIO.write(ImageIO.read(file), myImage.name(), out);
            out.close();
        } else {
            response.setValue("Content-Type", "text/plain");
            Writer writer = new OutputStreamWriter(response.getPrintStream());
            writer.write(FileUtils.readFileToString(file));
            writer.close();
        }
    }

}
