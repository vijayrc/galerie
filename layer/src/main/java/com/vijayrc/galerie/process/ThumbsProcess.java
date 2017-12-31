package com.vijayrc.galerie.process;

import com.vijayrc.galerie.domain.MyThumbs;
import com.vijayrc.galerie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Arrays.asList;

@Service
public class ThumbsProcess {
    @Autowired
    private ImageService imageService;
    @Autowired
    private NamingService namingService;
    @Autowired
    private IndexService indexService;

    public MyThumbs execute(String path) throws Exception {
        final MyThumbs myThumbs = new MyThumbs().withFile(path);
        for (ThumbsService service : asList(namingService, imageService, indexService))
            service.run(myThumbs);
        return myThumbs;
    }
}
