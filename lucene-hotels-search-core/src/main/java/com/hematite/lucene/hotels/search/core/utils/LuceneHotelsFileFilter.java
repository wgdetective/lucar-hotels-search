package com.hematite.lucene.hotels.search.core.utils;

import java.io.File;
import java.io.FileFilter;

public class LuceneHotelsFileFilter implements FileFilter {

    @Override
    public boolean accept(final File pathname) {
        return pathname.getName().toLowerCase().endsWith(".csv");
    }
}
