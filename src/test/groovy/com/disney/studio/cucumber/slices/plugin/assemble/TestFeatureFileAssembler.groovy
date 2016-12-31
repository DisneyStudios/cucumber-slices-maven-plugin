package com.disney.studio.cucumber.slices.plugin.assemble

import com.disney.studio.cucumber.slices.plugin.disassemble.FeatureFileCollector
import com.disney.studio.cucumber.slices.plugin.disassemble.FeatureFileParser
import org.junit.Ignore
import org.junit.Test


class TestFeatureFileAssembler {

    /*
    Passing a malformed tag to the constructor should throw an AssertionError that reads as follows:
    "Missing '@' character!!! One or more of the supplied Cucumber tags, '[test-tag]', is not properly formatted in the POM file"
    */
    @Test(expected = AssertionError)
    void shouldFailDueToMalformedTag() {
        new FeatureFileAssembler(['test-tag'])
    }

    @Test(expected = AssertionError)
    void shouldFailDueToMalformedTags() {
        new FeatureFileAssembler(['@test-tag1', 'test-tag2'])
    }

    @Test
    void shouldPassWithProperlyFormattedTag() {
        FeatureFileAssembler featureFileAssembler = new FeatureFileAssembler(['@test-tag'])
        assert featureFileAssembler instanceof FeatureFileAssembler
    }

    @Test
    void shouldSupportOutlinesWithMultipleParamsPerStep() {
        FeatureFileCollector featureFileCollector = new FeatureFileCollector('src/it/outline-feature-multiple-params-per-step/src/test/resources/features')
        FeatureFileParser featureFileParser = new FeatureFileParser()
        List<String> featureFileNames = featureFileCollector.getFeatureFileNameCollection()

        // Initialize the assemble objects
        FeatureFileAssembler featureFileAssembler = new FeatureFileAssembler(['@regression'])
        featureFileNames.each { name ->
            // format the contents of the supplied feature file as JSON
            String json = featureFileParser.formatAsJson(name)

            // now, re-assemble the feature file contents (represented as JSON) into plain text files
            // with 1 scenario per feature file
            featureFileAssembler.assembleFeatureFileFromJson(json)
        }

        assert true
    }
}
