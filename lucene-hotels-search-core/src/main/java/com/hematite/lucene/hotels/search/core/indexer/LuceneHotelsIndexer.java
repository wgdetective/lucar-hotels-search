package com.hematite.lucene.hotels.search.core.indexer;

import liquibase.util.csv.CSVReader;
import lombok.extern.java.Log;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

import static com.hematite.lucene.hotels.search.core.constants.LuceneHotelsConstant.HOTEL_ID;
import static com.hematite.lucene.hotels.search.core.constants.LuceneHotelsConstant.HOTEL_NAME;
import static com.hematite.lucene.hotels.search.core.constants.LuceneHotelsConstant.LANG_ID;

@Log
public class LuceneHotelsIndexer {
    private final IndexWriter indexWriter;

    public LuceneHotelsIndexer(final String indexDirPath) throws IOException {
        final Directory directory = FSDirectory.open(Paths.get(indexDirPath));
        final StandardAnalyzer analyzer = new StandardAnalyzer();

        indexWriter = new IndexWriter(directory, new IndexWriterConfig(analyzer));
    }

    public void close() throws IOException {
        indexWriter.close();
    }

    public void createDocuments(final String dataDirPath, final FileFilter filter) {
        final File[] files = new File(dataDirPath).listFiles();

        if (files != null && files.length != 0) {
            log.info("Start indexing files");
            final Instant start = Instant.now();
            for (final File file : files) {
                if(!file.isDirectory()
                   && !file.isHidden()
                   && file.exists()
                   && file.canRead()
                   && filter.accept(file)
                ){
                    parseCsvFile(file);
                }
            }

            final Instant finish = Instant.now();
            final long timeElapsed = Duration.between(start, finish).toMillis();
            log.info("Files was indexed, total time: " + timeElapsed);
        } else {
            log.info("Directory is empty");
        }
    }

    private void parseCsvFile(final File file) {
        try {
            final CSVReader reader = new CSVReader(new FileReader(file));

            String[] values;
            while ((values = reader.readNext()) != null) {
                final Document document = new Document();
                document.add(new TextField(HOTEL_ID, values[0], Field.Store.YES)); // do we need this
                document.add(new TextField(LANG_ID, values[1], Field.Store.NO));
                document.add(new TextField(HOTEL_NAME, values[2], Field.Store.YES));
                indexWriter.addDocument(document);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
