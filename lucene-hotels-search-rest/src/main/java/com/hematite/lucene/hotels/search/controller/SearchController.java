package com.hematite.lucene.hotels.search.controller;

import com.hematite.lucene.hotels.search.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@AllArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/search")
    public List<String> search(@RequestParam final String searchString) {
        return searchService.search(searchString);
    }
}
