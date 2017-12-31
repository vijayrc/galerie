package com.vijayrc.galerie.service;

import com.vijayrc.galerie.domain.MyFile;
import com.vijayrc.galerie.domain.MyFileList;
import com.vijayrc.galerie.domain.MyThumbs;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.sandbox.queries.regex.RegexQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.vijayrc.simple.util.Util.userDir;
import static org.apache.commons.lang.ArrayUtils.addAll;
import static org.apache.lucene.util.Version.LUCENE_44;

@Service
public class IndexService implements ThumbsService{
    private static Logger log = Logger.getLogger(IndexService.class);

    private String indexDir;
    private StandardAnalyzer analyzer;
    private ExplorerService explorerService;

    @Autowired
    public IndexService(ExplorerService explorerService) throws Exception {
        this.indexDir = userDir()+"/lucene";
        this.explorerService = explorerService;
        this.analyzer = new StandardAnalyzer(LUCENE_44);
    }

    public MyFileList add(String path) throws Exception {
        log.info("index:start");
        File dir = new File(path);
        FSDirectory folder = FSDirectory.open(new File(indexDir));
        IndexWriterConfig config = new IndexWriterConfig(LUCENE_44, analyzer);
        IndexWriter writer = new IndexWriter(folder, config);

        MyFileList myFileList = explorerService.listFiles(dir);
        writer.deleteDocuments(new Term("path"));

        myFileList.all().forEach(f -> {
            try {
                writer.addDocument(makeDocument(f));
                log.info("index:"+f.path());
            } catch (Exception e) {
                log.error(e);
            }
        });
        writer.close();
        log.info("index:end");
        return myFileList;
    }

    public MyFileList search(String regex) throws Exception {
        FSDirectory folder = FSDirectory.open(new File(indexDir));
        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(folder));
        ScoreDoc[] scoreDocs = new ScoreDoc[0];
        try {
            RegexQuery query = new RegexQuery(new Term("path", regex));
            scoreDocs= (ScoreDoc[]) addAll(scoreDocs, searcher.search(query, 300).scoreDocs);
        } catch (Exception e) {
            log.error("error:"+e.getMessage());
        }
        List<MyFile> myFiles = new ArrayList<>();
        for (ScoreDoc scoreDoc : scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            myFiles.add(new MyFile(doc.get("name"),doc.get("path"),Long.parseLong(doc.get("size")))
                    .withTags(doc.get("tags")));
        }
        MyFileList myFileList = new MyFileList(myFiles);
        log.info("found:" + myFileList.all().size());
        return myFileList;
    }

    @Override
    public void run(MyThumbs myThumbs) throws Exception {
        MyFileList myFileList = add(myThumbs.file().getPath());
        myThumbs.withFileList(myFileList);
    }

    private Document makeDocument(MyFile f) {
        Document doc = new Document();
        doc.add(new TextField("name", f.name(), Field.Store.YES));
        doc.add(new TextField("path", f.path(), Field.Store.YES));
        doc.add(new TextField("tags", f.tags(), Field.Store.YES));
        doc.add(new TextField("size", f.longSize(), Field.Store.YES));
        return doc;
    }
}
