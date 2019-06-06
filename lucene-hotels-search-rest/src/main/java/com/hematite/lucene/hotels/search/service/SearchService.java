package com.hematite.lucene.hotels.search.service;

import com.hematite.lucene.hotels.search.core.LuceneHotelsSearchService;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final LuceneHotelsSearchService luceneHotelsSearchService;

    @Value("${directory.path.data}")
    private String dataDirPath;

    @PostConstruct
    public void init() throws IOException {
        luceneHotelsSearchService.generateIndexes(dataDirPath);
    }

    public List<String> search(final String searchString, final String langId)
        throws IOException, ParseException {
        return luceneHotelsSearchService.search(searchString, langId);
    }

    public String addIndexes(final String dataDirPath) throws IOException {
        luceneHotelsSearchService.generateIndexes(dataDirPath);
        return "Success";
    }

    public String deleteIndexes(final String dataDirPath) throws IOException {
        luceneHotelsSearchService.deleteIndexes(dataDirPath);
        return "Success";
    }

    public String updateIndexes(final String dataDirPath) throws IOException {
        luceneHotelsSearchService.updateIndexes(dataDirPath);
        return "Success";
    }
}
