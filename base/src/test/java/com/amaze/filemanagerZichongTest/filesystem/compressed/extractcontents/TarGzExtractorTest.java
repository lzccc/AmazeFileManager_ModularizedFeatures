package com.amaze.filemanagerZichongTest.filesystem.compressed.extractcontents;

import com.amaze.filemanagerZichongTest.filesystem.compressed.extractcontents.helpers.GzipExtractor;

public class TarGzExtractorTest extends AbstractExtractorTest {
    @Override
    protected String getArchiveType() {
        return "tar.gz";
    }

    @Override
    protected Class<? extends Extractor> extractorClass() {
        return GzipExtractor.class;
    }
}
