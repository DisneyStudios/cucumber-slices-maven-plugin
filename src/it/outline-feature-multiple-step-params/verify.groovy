import groovy.io.FileType

File runner0 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner0.groovy')
File runner1 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner1.groovy')
File runner2 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner2.groovy')
File parallelFeatures = new File(basedir, 'src/test/resources/parallel_features')

assert runner0.exists()
assert runner1.exists()
assert runner2.exists()

String actualContent0 = ''
String actualContent1 = ''
String actualContent2 = ''

int actualNumberOfFeatureFiles = 0
parallelFeatures.eachFileRecurse(FileType.FILES) { featureFile ->
    if (featureFile.name.contains('the-search-for-Mickey-Mouse')) {
        actualContent0 = featureFile.text
    }
    if (featureFile.name.contains('the-search-for-Donald-Duck')) {
        actualContent1 = featureFile.text
    }
    if (featureFile.name.contains('the-search-for-Walt-Disney')) {
        actualContent2 = featureFile.text
    }

    actualNumberOfFeatureFiles++
}

assert actualNumberOfFeatureFiles == 3

assert actualContent0 == """\
Feature: Searching Google Using Scenario Outline With An Embedded Dollar Sign In Examples:  The search for "\$Mickey Mouse"



@regression
Scenario: The search for "\$Mickey Mouse"
Given I am on Google home page
When I enter the keyword of "\$Mickey Mouse"
And click the Submit button
Then the page title returned is "\$Mickey Mouse - Google Search"

"""

assert actualContent1 == """\
Feature: Searching Google Using Scenario Outline With An Embedded Dollar Sign In Examples:  The search for Donald Duck



@regression
Scenario: The search for Donald Duck
Given I am on Google home page
When I enter the keyword of Donald Duck
And click the Submit button
Then the page title returned is Donald Duck - Google Search

"""

assert actualContent2 == """\
Feature: Searching Google Using Scenario Outline With An Embedded Dollar Sign In Examples:  The search for Walt Disney



@regression
Scenario: The search for Walt Disney
Given I am on Google home page
When I enter the keyword of Walt Disney
And click the Submit button
Then the page title returned is Walt Disney - Google Search

"""
