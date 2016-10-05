package com.disney.studio.cucumber.slices.plugin.assemble

import groovy.io.FileType
import org.junit.BeforeClass
import org.junit.Test

class TestFeatureFileWriter {
    private static FeatureFileWriter featureFileWriter
    private static File featurePath
    private static File featureFile

    @BeforeClass
    static void setup() {
        featureFileWriter = new FeatureFileWriter()
        featurePath = new File('src/test/resources/features')
        if (!featurePath.exists()) {
            featurePath.mkdir()
        }
        featureFile = new File('src/test/resources/features/test.feature')
    }

    private static void removeAllFeatureFiles() {
        // remove all feature files
        featurePath.eachFileRecurse(FileType.FILES) { file ->
            if (file.name =~ /.*\.feature/) {
                file.delete()
            }
        }
    }

    @Test(expected = IllegalArgumentException)
    void shouldFailDueToMissingFileHandle() {
        List<String> featureInformation = ['Feature: My Test Feature', 'Description of feature']
        featureFileWriter.file = null
        featureFileWriter.writeFeature([featureInformation])
    }

    @Test
    void shouldWriteFeatureFileNarrative() {
        removeAllFeatureFiles()

        List<String> featureInformation = ['Feature: My Test Feature'
                                           , "As a casual user\nI want the ability to use Google's search feature\nSo that I can retrieve useful and accurate information"]
        featureFileWriter.file = featureFile
        featureFileWriter.writeFeature([featureInformation])

        assert featureFile.text == """\
Feature: My Test Feature
As a casual user
I want the ability to use Google's search feature
So that I can retrieve useful and accurate information

"""
    }

    @Test
    void shouldWriteFeatureFileNarrativeBackgroundGeneralSteps() {
        removeAllFeatureFiles()

        List<String> featureInformation = ['Feature: Searching Google Using Background']
        List<String> backgroundInformation = ['Background: Initial step', 'Given I am on Google home page']
        List<String> generalStepsInformation = ['@smoke @regression', 'Scenario: The search for Java'
                                                , 'When I enter the keyword of "Java"', 'And click the Submit button'
                                                , 'Then the page title returned is "Java - Google Search"']

        featureFileWriter.file = featureFile
        featureFileWriter.writeFeature([featureInformation, backgroundInformation, generalStepsInformation])

        assert featureFile.text == """\
Feature: Searching Google Using Background

Background: Initial step
Given I am on Google home page

@smoke @regression
Scenario: The search for Java
When I enter the keyword of "Java"
And click the Submit button
Then the page title returned is "Java - Google Search"

"""
    }
}
