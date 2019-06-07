# Lucene hotels search
Time for processing 90 000 queries: 150 000 ms*  

\* 200 000 rows

## Installation and getting started

1\.	Download lucene-hotels-search-core module from GitHub (https://github.com/wgdetective/lucar-hotels-search/tree/develop) and add maven dependency:
```
<dependency>
    <groupId>com.hematite</groupId>
    <artifactId>lucene-hotels-search-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
2\.	Initialize LuceneHotelsSearchService with directory which will contain indexes:

```
final LuceneHotelSearchService luceneHotelsSearchService = new LuceneHotelsSearchService(indexDirPath);
```

3\.	For adding/deleting/updating indexes call suitable method of LuceneHotelsSearchService with directory path, which contains .csv files with data:
```
luceneHotelsSearchService.generateIndexes(dataDirPath);

luceneHotelsSearchService.deleteIndexes(dataDirPath);

luceneHotelsSearchService.updateIndexes(dataDirPath);
```

4\.	For perform search call LuceneHotelsSearchService.search method, pass search value and lang id as parameter:  
```
final List<String> result = luceneHotelsSearchService.search(searchString, langId);
```
