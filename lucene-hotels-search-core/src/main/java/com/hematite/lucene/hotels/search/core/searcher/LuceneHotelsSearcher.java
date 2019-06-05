package com.hematite.lucene.hotels.search.core.searcher;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

import static com.hematite.lucene.hotels.search.core.constants.LuceneHotelsConstant.HOTEL_NAME;
import static com.hematite.lucene.hotels.search.core.constants.LuceneHotelsConstant.MAX_SEARCH;

public class LuceneHotelsSearcher {
    private final IndexSearcher indexSearcher;
    private final QueryParser parser;

    public LuceneHotelsSearcher(final String indexDirPath)
        throws IOException {
        final Directory indexDirectory = FSDirectory.open(Paths.get(indexDirPath));
        final IndexReader reader = DirectoryReader.open(indexDirectory);
        indexSearcher = new IndexSearcher(reader);

        parser = new QueryParser(HOTEL_NAME, new StandardAnalyzer());
        parser.setDefaultOperator(QueryParser.Operator.AND);
    }

    public TopDocs search(final String searchQuery)
        throws IOException, ParseException {
        final Query query;
        final String preparedQuery =
            searchQuery.replaceAll("[^a-zA-Z0-9\\s]", "").trim().toLowerCase();
        if (preparedQuery.isEmpty()) {
            query = new TermQuery(new Term(HOTEL_NAME, preparedQuery));
        } else {
            query = parser.parse(preparedQuery);
        }
        return indexSearcher.search(query, MAX_SEARCH);
    }

    public Document getDocument(final ScoreDoc scoreDoc) throws IOException {
        return indexSearcher.doc(scoreDoc.doc);
    }
}
