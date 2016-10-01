package com.disney.studio.cucumber.slices.plugin.assemble

import groovy.util.logging.Slf4j

import java.nio.file.Paths

@Slf4j
class CucumberRunWithWriter {
    private static final String CUKE_RUNNER_TEMPLATE_FILE = 'cuke_runner_template.txt'
    private static final String CUKE_RUNNER_TEMPLATE_EXAMPLE = """\
import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

@RunWith(Cucumber.class)
@CucumberOptions (
        features = ["classpath:parallel_features/<feature file>"]
        , monochrome = true
        , format = ["pretty", "json:target/cucumber-report/TestGroup<runner index>/cucumber.json", "rerun:rerun.txt", "junit:target/cucumber-report/TestGroup<runner index>/cucumber.xml"]
        , glue = ["src/test/groovy/com/disney/studio/qa/steps"]
        , tags = ["~@manual"]
)
class ParallelRunner<runner index> {
}
"""
    private String cukeRunnerTemplateCode
    private String parallelRunnersDirectory
    private String templatesDirectory
    private LinkedHashMap<String, String> assembledCucumberRunnerFiles

    CucumberRunWithWriter(String parallelRunnersDirectory, String templatesDirectory) {
        this.parallelRunnersDirectory = parallelRunnersDirectory
        this.templatesDirectory = templatesDirectory
        this.cukeRunnerTemplateCode = readCukeRunnerTemplate()
        this.assembledCucumberRunnerFiles = new LinkedHashMap<>()
        createParallelRunnerDirectory()
    }

    private String readCukeRunnerTemplate() {
        File templateDir = Paths.get(templatesDirectory).toFile()
        if (!templateDir.exists()) {
            throw new IllegalStateException("The templates resource directory [$templatesDirectory] does not exist!")
        }

        File templateFile = new File(templateDir.absolutePath.trim() + '/' + CUKE_RUNNER_TEMPLATE_FILE)
        if (!templateFile.exists()) {
            throw new IllegalStateException("The Cucumber Runner Template file [$templateFile] does not exist!! " +
                    "Be sure to create the runner file with the appropriate code.\n\nCucumber Runner Example (Groovy code):\n\n $CUKE_RUNNER_TEMPLATE_EXAMPLE")
        }

        return templateFile.text
    }

    private void createParallelRunnerDirectory() {
        File dir = new File("$parallelRunnersDirectory")
        if (dir.exists() || !dir.exists()) {
            dir.deleteDir()
            dir.mkdirs()
        }
        log.info("Successfully created the ${dir.absolutePath} directory.")
    }

    private String findAndReplaceCukeRunnerTokens(String fileName, int counter) {
        def newCode = (cukeRunnerTemplateCode =~ /<feature file>/).replaceFirst(fileName)
        newCode = (newCode =~ /<runner index>/).replaceAll(counter.toString())
        return newCode
    }

    void assembleCucumberRunnerFiles(FeatureFileAssembler featureFileAssembler) {
        int runCounter = 0

        for (featureFile in featureFileAssembler.featureFileWriter.featureFiles) {
            def cukeRunnerFileName = "$parallelRunnersDirectory/ParallelRunner${runCounter}.groovy"
            def featureFileName = Paths.get(featureFile.toString()).fileName.toString()
            assembledCucumberRunnerFiles[cukeRunnerFileName] = featureFileName
            runCounter++
        }
    }

    void writeCucumberRunWithFiles() {
        assert assembledCucumberRunnerFiles.size() > 0, 'Invalid collection!! The collection of assembled Cucumber runner files is empty'
        int runCounter = 0
        for (runner in assembledCucumberRunnerFiles) {
            // create file using the key, which represents the cuke runner filename
            File cukeRunnerFile = new File(runner.key)
            cukeRunnerFile.withWriter { writer ->
                // replace cuke runner tokens supplying the value of the map, which represents the feature file name
                writer.write(findAndReplaceCukeRunnerTokens(runner.value, runCounter))
            }
            runCounter++
            log.info("Successfully created Cuke Runner '${Paths.get(cukeRunnerFile.absolutePath).fileName}' " +
                    "for the following feature file: '${Paths.get(runner.value).fileName}'.")
        }
    }

}
