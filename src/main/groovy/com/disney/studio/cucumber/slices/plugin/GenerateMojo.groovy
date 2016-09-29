package com.disney.studio.cucumber.slices.plugin

import groovy.util.logging.Slf4j
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo

@Slf4j
@Mojo(name = 'generate', defaultPhase = LifecyclePhase.GENERATE_TEST_RESOURCES)
class GenerateMojo extends AbstractMojo {


    public void execute() throws MojoExecutionException, MojoFailureException {
        log.info("EXECUTING the 'generate' goal of the Cucumber Slices Plugin")
    }
}
