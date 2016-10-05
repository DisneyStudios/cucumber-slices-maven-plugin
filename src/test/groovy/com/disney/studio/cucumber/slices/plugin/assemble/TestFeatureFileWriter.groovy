package com.disney.studio.cucumber.slices.plugin.assemble

import groovy.io.FileType
import org.junit.BeforeClass
import org.junit.Test

import java.nio.file.Paths

class TestFeatureFileWriter {
    private static FeatureFileWriter featureFileWriter
    private static File featurePath
    private static File featureFile

    @BeforeClass
    static void setup() {
        featureFileWriter = new FeatureFileWriter()
        featurePath = Paths.get('src/test/resources/features').toFile()
        featureFile = Paths.get('src/test/resources/features/test.feature').toFile()

        // remove all feature files
        featurePath.eachFileRecurse(FileType.FILES) { file ->
            if (file.name =~ /.*\.feature/) {
                file.delete()
            }
        }
    }

    @Test
    void shouldFailDueToMissingFileHandle() {
        List<String> featureInformation = ['Feature: My Test Feature', 'Description of feature']
        featureFileWriter.writeFeature([featureInformation])
    }

    @Test
    void shouldWriteFeatureFileNarrative() {
        List<String> featureInformation = ['Feature: My Test Feature', "As a casual user\nI want the ability to use Google's search feature\nSo that I can retrieve useful and accurate information"]
        featureFileWriter.file = featureFile
        featureFileWriter.writeFeature(featureInformation)
        println ''

    }
}
