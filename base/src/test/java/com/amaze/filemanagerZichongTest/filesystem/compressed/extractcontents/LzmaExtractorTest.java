package com.amaze.filemanagerZichongTest.filesystem.compressed.extractcontents;

import com.amaze.filemanagerZichongTest.filesystem.compressed.extractcontents.helpers.LzmaExtractor;

public class LzmaExtractorTest extends AbstractExtractorTest {
    @Override
    protected String getArchiveType() {
        return "tar.lzma";
    }

    @Override
    protected Class<? extends Extractor> extractorClass() {
        return LzmaExtractor.class;
    }
}
