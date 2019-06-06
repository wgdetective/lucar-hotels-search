package com.hematite.lucene.hotels.search.controller;

import com.hematite.lucene.hotels.search.service.SearchService;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/search")
    public List<String> search(@RequestParam final String searchString)
        throws IOException, ParseException {
        return searchService.search(searchString);
    }

    @GetMapping("/add_indexes")
    public String addIndexes(@RequestParam final String dataDirPath) throws IOException {
        return searchService.addIndexes(dataDirPath);
    }

    @GetMapping("/delete_indexes")
    public String deleteIndexes(@RequestParam final String dataDirPath) throws IOException {
        return searchService.deleteIndexes(dataDirPath);
    }

    @GetMapping("/update_indexes")
    public String updateIndexes(@RequestParam final String dataDirPath) throws IOException {
        return searchService.updateIndexes(dataDirPath);
    }
}
