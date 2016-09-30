package com.disney.studio.cucumber.slices.plugin

import com.disney.studio.cucumber.slices.plugin.disassemble.FeatureFileCollector
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

    @Parameter(property = 'cucumberTags')
    private List<String> cucumberTags

    @Parameter(property = 'parallelRunnersDirectory', required = true)
    private String parallelRunnersDirectory

    @Parameter(property = 'featuresDirectory', defaultValue = 'src/test/resources/features')
    private String featuresDirectory

    public void execute() throws MojoExecutionException, MojoFailureException {
        log.info("EXECUTING the 'generate' goal of the Cucumber Slices Plugin . . .")
        log.info("Cucumber Tags: ${cucumberTags}")
        log.info("Output Directory for Parallel Runners: $parallelRunnersDirectory")
        log.info("Cucumber Features Directory: $featuresDirectory")

        FeatureFileCollector featureFileCollector = new FeatureFileCollector(featuresDirectory)
        List<String> featureFileNames = featureFileCollector.getFeatureFileNameCollection()
        log.info("List of Feature File Names: $featureFileNames")
    }
}
