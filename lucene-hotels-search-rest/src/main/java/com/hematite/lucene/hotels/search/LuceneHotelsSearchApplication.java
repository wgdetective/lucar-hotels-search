package com.hematite.lucene.hotels.search;

import com.hematite.lucene.hotels.search.core.LuceneHotelsSearchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class LuceneHotelsSearchApplication {
    @Value("${directory.path.index}")
    private String indexDirPath;

    @Bean
    public LuceneHotelsSearchService luceneHotelsSearchService() throws IOException {
        return new LuceneHotelsSearchService(indexDirPath);
    }

    public static void main(final String[] args) {
        SpringApplication.run(LuceneHotelsSearchApplication.class, args);
    }

}
