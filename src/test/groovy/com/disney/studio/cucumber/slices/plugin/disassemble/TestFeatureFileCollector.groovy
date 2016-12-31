package com.disney.studio.cucumber.slices.plugin.disassemble

import org.junit.Ignore
import org.junit.Test

class TestFeatureFileCollector {

    @Test
    void shouldAcceptFeaturePathInConstructor() {
        String featuresDirPath = '/my/test/features/dir'
        FeatureFileCollector featureFileCollector = new FeatureFileCollector(featuresDirPath)
        assert featureFileCollector.dump().contains(featuresDirPath)
    }

    @Test(expected = IllegalArgumentException)
    void shouldFailToGetCollectionIfMissingDirectory() {
        String featuresDirPath = '/my/bogus/features/dir'
        FeatureFileCollector featureFileCollector = new FeatureFileCollector(featuresDirPath)
        featureFileCollector.getFeatureFileNameCollection()
    }

    @Test
    void shouldReturnFeatureFileCollection() {
        String featuresDirPath = 'src/it/multiple-feature-files/src/test/resources/features'
        FeatureFileCollector featureFileCollector = new FeatureFileCollector(featuresDirPath)
        List<String> featureFiles = featureFileCollector.getFeatureFileNameCollection()
        assert featureFiles.size() > 0
        assert featureFiles.every { fileName ->
            fileName =~ /.*\.feature/
        }
    }
}
