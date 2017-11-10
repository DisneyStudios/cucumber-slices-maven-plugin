package com.disney.studio.cucumber.slices.plugin.disassemble

import groovy.io.FileType
import groovy.util.logging.Slf4j

import java.nio.file.Paths

@Slf4j
class FeatureFileCollector {
    private String featuresDirPath
    private List<String> featureFileNames = []

    FeatureFileCollector(String featureDirPath) {
        this.featuresDirPath = featureDirPath
    }

    List<String> getFeatureFileNameCollection() {
        if (!featureFileNames) {
            collectFeatureFileNames()
        }
        return featureFileNames
    }

    private void collectFeatureFileNames() {
        File directory = new File(featuresDirPath)
        if (!directory.exists()) {
            throw new IllegalArgumentException("The supplied features directory [${directory.absolutePath}] does not exist!!")
        }
        log.debug("Recursively scanning the supplied Cucumber features directory [${directory.absolutePath}]...")

        directory.eachFileRecurse(FileType.FILES) { file ->
            if (file.name =~ /.*\.feature/) {
                featureFileNames.add(file.absolutePath)
            }
        }

        log.debug("Total number of feature files scanned: ${featureFileNames.size()}")
    }
}
