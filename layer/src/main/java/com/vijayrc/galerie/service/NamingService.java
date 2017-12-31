package com.vijayrc.galerie.service;

import com.vijayrc.galerie.domain.MyThumbs;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.File;

import static java.util.Arrays.asList;

@Service
public class NamingService extends FileService implements ThumbsService {
    private static Logger log = Logger.getLogger(NamingService.class);

    @Override
    public void run(MyThumbs myThumbs) {
        shortenNames(myThumbs.file());
    }

    public void shortenNames(File dir){
        if(isInvalidFolder(dir) || isEmpty(dir))
            return;
        asList(dir.listFiles()).forEach(f -> {
            String name = f.getName();
            if(name.length() < 40)
                return;
            int typeIndex = name.contains(".")? name.lastIndexOf('.'):name.length()-1;
            String title = name.substring(0, typeIndex);
            String type = name.substring(typeIndex);
            String px = dir.getAbsolutePath() + "/" + title.substring(0, 30) + "_";
            int sx = 1;

            File renamed = new File(px+sx+type);
            while(renamed.exists()) renamed = new File(px+(++sx)+type);
            f.renameTo(renamed);
            log.info("renamed:"+renamed);
        });
    }
}
