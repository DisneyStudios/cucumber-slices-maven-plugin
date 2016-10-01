# cucumber-slices-maven-plugin
The Cucumber Slices Plugin is designed to parse Cucumber feature files with 1 or more scenarios into many feature files, each with 1 scenario per feature file. The plugin can be used in combination with either the [Maven Surefire Plugin](http://maven.apache.org/surefire/maven-surefire-plugin/) or the [Maven Failsafe Plugin](http://maven.apache.org/surefire/maven-failsafe-plugin/) to run each of the generated feature files in parallel. Further information on how to configure these plugins to run Cucumber feature files in parallel is discussed below.

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
