package com.hematite.lucene.hotels.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LuceneHotelsSearchApplication {

   public static void main(final String[] args) {
        SpringApplication.run(LuceneHotelsSearchApplication.class, args);
    }

}
