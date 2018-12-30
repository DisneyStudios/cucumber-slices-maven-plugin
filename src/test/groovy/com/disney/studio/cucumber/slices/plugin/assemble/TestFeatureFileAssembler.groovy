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
    void shouldUseForDebuggingFeatureFiles() {
        String parallelRunnersDirectory = 'src/test/groovy/parallel_runners'
        String templatesDirectory = 'src/test/resources/templates'
        CucumberRunnerExtension cucumberRunnerExtension = CucumberRunnerExtension.groovy
        // To DEBUG different scenarios, simply replace the 'feature file' path within the FeatureFileCollector's
        // constructor
        FeatureFileCollector featureFileCollector = new FeatureFileCollector('src/it/outline-feature/src/test/resources/features')
        FeatureFileParser featureFileParser = new FeatureFileParser()
        List<String> featureFileNames = featureFileCollector.getFeatureFileNameCollection()

        // Initialize the assemble objects
        FeatureFileAssembler featureFileAssembler = new FeatureFileAssembler(['@regression'])
        CucumberRunWithWriter cucumberRunWithWriter = new CucumberRunWithWriter(parallelRunnersDirectory, templatesDirectory)

        featureFileNames.each { name ->
            // format the contents of the supplied feature file as JSON
            String json = featureFileParser.formatAsJson(name)

            // now, re-assemble the feature file contents (represented as JSON) into plain text files
            // with 1 scenario per feature file
            featureFileAssembler.assembleFeatureFileFromJson(json)

            // assemble the Cucumber Runner files. This will place each named feature file into a linked hash map
            cucumberRunWithWriter.assembleCucumberRunnerFiles(featureFileAssembler, cucumberRunnerExtension)
        }

        cucumberRunWithWriter.writeCucumberRunWithFiles()

        assert true
    }
}
