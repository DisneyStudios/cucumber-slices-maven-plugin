package com.disney.studio.cucumber.slices.plugin

import groovy.util.logging.Slf4j
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


/**
 * Goal which touches a timestamp file.
 *
 * @deprecated Don't use!
 */
@Slf4j
@Mojo( name = "touch", defaultPhase = LifecyclePhase.PROCESS_SOURCES )
class MyMojo
        extends AbstractMojo
{
    /**
     * Location of the file.
     */
    @Parameter( defaultValue = '${project.build.directory}', property = "outputDir", required = true )
    private File outputDirectory;

    public void execute()
            throws MojoExecutionException
    {
        File f = outputDirectory;

        if ( !f.exists() )
        {
            f.mkdirs();
        }

        File touch = new File( f, "touch.txt" );

        FileWriter w = null;
        try
        {
            w = new FileWriter( touch );
            System.out.println("Creating 'touch.txt' file in the " + f + " directory.");
            log.info("Creating 'touch.txt' file in the " + f + " directory.");
            w.write( "touch.txt" );
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( "Error creating file " + touch, e );
        }
        finally
        {
            if ( w != null )
            {
                try
                {
                    w.close();
                }
                catch ( IOException ignore )
                {
                    // ignore
                }
            }
        }
    }
}

