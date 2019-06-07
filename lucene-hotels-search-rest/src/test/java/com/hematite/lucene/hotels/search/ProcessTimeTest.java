package com.hematite.lucene.hotels.search;

import com.hematite.lucene.hotels.search.core.object.HotelObject;
import com.hematite.lucene.hotels.search.service.SearchService;
import lombok.extern.java.Log;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class ProcessTimeTest {

    @Autowired
    private SearchService searchService;

    @Test
    public void testLuceneProcessTime()
        throws IOException, ParseException, URISyntaxException {
        final List<String> lines =
            Files.readAllLines(Paths.get(ClassLoader.getSystemResource("hotelsQuery.txt").toURI()));
        final List<HotelObject> hotelObjects = new ArrayList<>();
        lines.forEach(line -> {
            final String[] values = line.split("//");
            hotelObjects.add(new HotelObject(null, values[1], values[0]));
        });

        final Instant start = Instant.now();
        for (final HotelObject value : hotelObjects) {
            searchService.search(value.getLangId(), value.getHotelName());
        }
        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();
        log.info("Time for processing queries with lucene: " + timeElapsed);
    }
}

