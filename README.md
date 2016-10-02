# cucumber-slices-maven-plugin
The Cucumber Slices Plugin is designed to parse Cucumber feature files with 1 or more scenarios into many feature files, each with 1 scenario per feature file. The plugin can be used in combination with either the [Maven Surefire Plugin](http://maven.apache.org/surefire/maven-surefire-plugin/) or the [Maven Failsafe Plugin](http://maven.apache.org/surefire/maven-failsafe-plugin/) to run each of the generated feature files in parallel. Further information on how to configure these plugins to run Cucumber feature files in parallel is discussed below.

Although not required, using the Cucumber Slices Plugin _after_ the `test-compile` lifecycle phase is NOT RECOMMENDED.  The purpose behind the plugin is to generate (at runtime), Cucumber Feature files and Cucumber Runners, by reading from an existing set of feature files.  In order for the plugin to work properly, it is RECOMMENDED that the execution phase is `generate-test-resources`, which is triggered before the `test-compile` phase.

## Usage

Simply add the following to the `plugins` section of your POM file

```xml
        <plugins>
            <plugin>
                <groupId>com.disney.studio.cucumber.slices.plugin</groupId>
                <artifactId>cucumber-slices-maven-plugin</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <executions>
                    <execution>
                        <phase>generate-test-resources</phase>
                        <goals>
                            <goal>generate</goal>
                        </goals>
                        <configuration>
                            <cucumberTags>
                                <param>@regression</param>
                            </cucumberTags>
                            <templatesDirectory>src/test/resources/templates</templatesDirectory>
                            <parallelRunnersDirectory>src/test/groovy/parallel_runners</parallelRunnersDirectory>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
```

Cucumber Slices supports the following `<configuration>` settings

setting | description | default value | property | required
--------|-------------|----------------|----------|----------
parallelRunnersDirectory | A directory that shall contain the Cucumber runner class files. The files contained within this directory are created at runtime. The directory **MUST** exist somewhere on the _test_ classpath | | parallelRunnersDirectory | YES |
templatesDirectory | A directory whose contents contain references to templates used within the plugin. This directory **MUST** contain the file labeled _cuke_runner_template.txt_. An example of the template file (Groovy-specific) is contained in the **example_template** directory within the root of this repository. | | templatesDirectory | YES |
featuresDirectory | A directory containing the Cucumber feature files. The directory **MUST** be in the root of the runtime classpath | src/test/resources/features | featuresDirectory | YES |
cucumberTags | A list of tags used by the plugin to filter which scenarios shall be read | | cucumberTags | NO |

## Usage Examples

### Example 1

