package com.amaze.filemanagerZichongTest.filesystem.compressed.extractcontents;

import com.amaze.filemanagerZichongTest.filesystem.compressed.extractcontents.helpers.RarExtractor;

public class RarExtractorTest extends AbstractExtractorTest {
    @Override
    protected String getArchiveType() {
        return "rar";
    }

    @Override
    protected Class<? extends Extractor> extractorClass() {
        return RarExtractor.class;
    }
}
