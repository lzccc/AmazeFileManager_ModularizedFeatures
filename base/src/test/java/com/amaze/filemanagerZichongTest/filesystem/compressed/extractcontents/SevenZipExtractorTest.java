package com.amaze.filemanagerZichongTest.filesystem.compressed.extractcontents;

import com.amaze.filemanagerZichongTest.filesystem.compressed.extractcontents.helpers.SevenZipExtractor;

public class SevenZipExtractorTest extends AbstractExtractorTest {
    @Override
    protected String getArchiveType() {
        return "7z";
    }

    @Override
    protected Class<? extends Extractor> extractorClass() {
        return SevenZipExtractor.class;
    }
}
