package com.disney.studio.cucumber.slices.plugin.disassemble

import groovy.io.FileType
import groovy.util.logging.Slf4j

@Slf4j
class FeatureFileCollector {
    private String featuresDirPath
    private List<String> featureFileNames = []

    FeatureFileCollector() {
        this('src/test/resources/features')
    }

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
        log.info("Recursivley scanning the supplied Cucumber features directory [${directory.absolutePath}] . . .")

        directory.eachFileRecurse(FileType.FILES) { file ->
            if (file.name =~ /.*\.feature/) {
                featureFileNames.add(file.absolutePath)
            }
        }
    }
}
