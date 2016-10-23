package com.disney.studio.cucumber.slices.plugin

import com.disney.studio.cucumber.slices.plugin.assemble.CucumberRunWithWriter
import com.disney.studio.cucumber.slices.plugin.assemble.CucumberRunnerExtension
import com.disney.studio.cucumber.slices.plugin.assemble.FeatureFileAssembler
import com.disney.studio.cucumber.slices.plugin.disassemble.FeatureFileCollector
import com.disney.studio.cucumber.slices.plugin.disassemble.FeatureFileParser
import groovy.util.logging.Slf4j
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter

/**
 * Generates a set of Cucumber feature files and runners from an existing list of feature files. Each of the generated
 * feature files has 1 scenario and each of the generated Cucumber runner files is associated with 1 feature file.
 */
@Slf4j
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_TEST_RESOURCES)
class GenerateMojo extends AbstractMojo {

    @Parameter(property = 'parallelRunnersDirectory', required = true)
    private String parallelRunnersDirectory

    @Parameter(property = 'templatesDirectory', required = true)
    private String templatesDirectory

    @Parameter(defaultValue = 'src/test/resources/features', property = 'featuresDirectory', required = true)
    private String featuresDirectory

    @Parameter(property = 'cucumberTags')
    private List<String> cucumberTags

    @Parameter(defaultValue = "groovy", property = 'cucumberRunnerExtension')
    private CucumberRunnerExtension cucumberRunnerExtension

    public void execute() throws MojoExecutionException, MojoFailureException {
        // Initialize the disassemble objects
        FeatureFileCollector featureFileCollector = new FeatureFileCollector(featuresDirectory)
        FeatureFileParser featureFileParser = new FeatureFileParser()
        List<String> featureFileNames = featureFileCollector.getFeatureFileNameCollection()

        // Initialize the assemble objects
        FeatureFileAssembler featureFileAssembler = new FeatureFileAssembler(cucumberTags)
        CucumberRunWithWriter cucumberRunWithWriter = new CucumberRunWithWriter(parallelRunnersDirectory, templatesDirectory)

        if (featureFileNames.size() == 0) {
            log.warn("Missing feature files!! Skipping further processing.")
        } else {
            featureFileNames.each { name ->
                // format the contents of the supplied feature file as JSON
                String json = featureFileParser.formatAsJson(name)

                // now, re-assemble the feature file contents (represented as JSON) into plain text files
                // with 1 scenario per feature file
                featureFileAssembler.assembleFeatureFileFromJson(json)

                // assemble the Cucumber Runner files. This will place each named feature file into a linked hash map
                cucumberRunWithWriter.assembleCucumberRunnerFiles(featureFileAssembler)
            }

            // write out the Cucumber Runner files
            log.info("Creating the Cucumber Runner files...")
            cucumberRunWithWriter.writeCucumberRunWithFiles()
        }


    }
}
