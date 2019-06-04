package com.hematite.lucene.hotels.search.core;

import com.hematite.lucene.hotels.search.core.indexer.LuceneHotelsIndexer;
import com.hematite.lucene.hotels.search.core.searcher.LuceneHotelsSearcher;
import com.hematite.lucene.hotels.search.core.utils.LuceneHotelsFileFilter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.hematite.lucene.hotels.search.core.constants.LuceneHotelsConstant.HOTEL_NAME;

public class LuceneHotelsSearchService {
    private final LuceneHotelsIndexer indexer;
    private final String indexDirPath;

    public LuceneHotelsSearchService(final String indexDirPath) throws IOException {
        indexer = new LuceneHotelsIndexer(indexDirPath);
        this.indexDirPath = indexDirPath;
    }

    public void generateIndexes(final String dataDirPath) throws IOException {
        indexer.createDocuments(dataDirPath, new LuceneHotelsFileFilter());
        indexer.close();
    }

    public List<String> search(final String searchQuery) throws IOException, ParseException {
        final LuceneHotelsSearcher searcher = new LuceneHotelsSearcher(indexDirPath);

        final TopDocs searchResult = searcher.search(searchQuery);
        final List<String> result = new ArrayList<>();

        for(final ScoreDoc scoreDoc : searchResult.scoreDocs) {
            result.add(searcher.getDocument(scoreDoc).get(HOTEL_NAME));
        }

        return result;
    }
}
