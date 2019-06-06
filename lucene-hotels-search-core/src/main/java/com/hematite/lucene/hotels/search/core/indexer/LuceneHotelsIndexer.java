package com.hematite.lucene.hotels.search.core.indexer;

import com.hematite.lucene.hotels.search.core.constants.LuceneOperationType;
import com.hematite.lucene.hotels.search.core.object.HotelObject;
import liquibase.util.csv.CSVReader;
import lombok.extern.java.Log;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    public void commit() throws IOException {
        indexWriter.commit();
    }

    public void editDocuments(final String dataDirPath,
                              final FileFilter filter,
                              final LuceneOperationType operationType)
        throws IOException {
        final File[] files = new File(dataDirPath).listFiles();

        if (files != null && files.length != 0) {
            processFiles(files, filter, operationType);
        } else {
            log.info("Directory is empty");
        }
    }

    private void processFiles(final File[] files,
                              final FileFilter filter,
                              final LuceneOperationType operationType)
        throws IOException {
        log.info("Start processing files");
        final Instant start = Instant.now();

        for (final File file : files) {

            if (!file.isDirectory() &&
                !file.isHidden() &&
                file.exists() &&
                file.canRead() &&
                filter.accept(file)) {

                final List<HotelObject> hotelObjects = processCsv(file);
                switch (operationType) {
                    case CREATE_DOCUMENTS: addDocuments(hotelObjects); break;
                    case DELETE_DOCUMENTS: deleteDocuments(hotelObjects); break;
                    case UPDATE_DOCUMENTS: updateDocuments(hotelObjects); break;
                }
            }
        }

        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();
        log.info(String.format("Files was processed, total time: %s", timeElapsed));
    }

    private List<HotelObject> processCsv(final File file) throws IOException {
        final CSVReader reader = new CSVReader(new FileReader(file));
        final List<HotelObject> hotelObjects = new ArrayList<>();

        HotelObject previousHotel = null;
        String[] values;
        while ((values = reader.readNext()) != null) {
            if (previousHotel == null || !previousHotel.getHotelId().equals(values[0])) {
                final HotelObject hotelObject = new HotelObject(values[0], values[1], values[2]);
                hotelObjects.add(hotelObject);
                previousHotel = hotelObject;
            }
        }
        return hotelObjects;
    }

    private void addDocuments(final List<HotelObject> hotelObjects) throws IOException {
        for (final HotelObject hotelObject : hotelObjects) {
            indexWriter.addDocument(createDocument(hotelObject));
        }
    }

    private void deleteDocuments(final List<HotelObject> hotelObjects) throws IOException {
        for (final HotelObject hotelObject : hotelObjects) {
            indexWriter.deleteDocuments(new Term(HOTEL_ID, hotelObject.getHotelId()));
        }
    }

    private void updateDocuments(final List<HotelObject> hotelObjects) throws IOException {
        for (final HotelObject hotelObject : hotelObjects) {
            indexWriter.updateDocument(new Term(HOTEL_ID, hotelObject.getHotelId()), createDocument(hotelObject));
        }
    }

    private Document createDocument(final HotelObject hotelObject) {
        final Document document = new Document();
        document.add(new TextField(HOTEL_ID, hotelObject.getHotelId(), Field.Store.YES));
        document.add(new TextField(LANG_ID, hotelObject.getLangId(), Field.Store.NO));
        document.add(new TextField(HOTEL_NAME, hotelObject.getHotelName(), Field.Store.YES));
        return document;
    }
}
