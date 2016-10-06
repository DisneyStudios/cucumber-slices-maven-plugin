import groovy.io.FileType

File runner0 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner0.groovy')
File runner1 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner1.groovy')
File parallelFeatures = new File(basedir, 'src/test/resources/parallel_features')

assert runner0.exists()
assert runner1.exists()

String fileContent0 = ''
String fileContent1 = ''

parallelFeatures.eachFileRecurse(FileType.FILES) { featureFile ->
    if (featureFile.name.contains('the-search-for-java')) {
        fileContent0 = featureFile.text
    }
    if (featureFile.name.contains('the-search-for-sauce-labs')) {
        fileContent1 = featureFile.text
    }
}

assert fileContent0 == """\
Feature: Searching Google Using Background:  The search for Java


Background: Initial step
Given I am on Google home page

@smoke @regression
Scenario: The search for Java
When I enter the keyword of "Java"
And click the Submit button
Then the page title returned is "Java - Google Search"

"""

assert fileContent1 == """\
Feature: Searching Google Using Background:  The search for Sauce Labs


Background: Initial step
Given I am on Google home page

@regression
Scenario: The search for Sauce Labs
When I enter the keyword of "Sauce Labs"
And click the Submit button
Then the page title returned is "Sauce Labs - Google Search"

"""
