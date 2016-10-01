package com.disney.studio.cucumber.slices.plugin

import com.disney.studio.cucumber.slices.plugin.assemble.CucumberRunWithWriter
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

@Slf4j
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_TEST_RESOURCES)
class GenerateMojo extends AbstractMojo {

    @Parameter(property = 'parallelRunnersDirectory', required = true)
    private String parallelRunnersDirectory

    @Parameter(property = 'templatesDirectory', required = true)
    private String templatesDirectory

    @Parameter(property = 'cucumberTags')
    private List<String> cucumberTags

    @Parameter(defaultValue = 'src/test/resources/features', property = 'featuresDirectory')
    private String featuresDirectory

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("EXECUTING the 'generate' goal of the Cucumber Slices Plugin . . .")
        getLog().info("Cucumber Tags: ${cucumberTags}")
        getLog().info("Output Directory for Parallel Runners: $parallelRunnersDirectory")
        log.info("Cucumber Features Directory: $featuresDirectory")
        log.info("Cucumber Templates Directory: $templatesDirectory")

        // Initialize the disassemble objects
        FeatureFileCollector featureFileCollector = new FeatureFileCollector(featuresDirectory)
        FeatureFileParser featureFileParser = new FeatureFileParser()
        List<String> featureFileNames = featureFileCollector.getFeatureFileNameCollection()

        // Initialize the assemble objects
        FeatureFileAssembler featureFileAssembler = new FeatureFileAssembler(cucumberTags)
        CucumberRunWithWriter cucumberRunWithWriter = new CucumberRunWithWriter(parallelRunnersDirectory)

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
        log.info("Creating the Cucumber RunWith Files . . .")
        cucumberRunWithWriter.writeCucumberRunWithFiles()
    }
}
