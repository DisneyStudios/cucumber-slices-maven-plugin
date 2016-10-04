package com.disney.studio.cucumber.slices.plugin.assemble

import org.junit.Test

class TestFeatureFileWriter {


    @Test
    void shouldFailDueToMissingFileHandle() {
        List<String> featureInformation = ['Feature: My Test Feature', 'Description of feature']
        FeatureFileWriter featureFileWriter = new FeatureFileWriter()
        featureFileWriter.writeFeature([featureInformation])
    }
}
