import groovy.io.FileType

File runner0 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner0.groovy')
File runner1 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner1.groovy')
File runner2 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner2.groovy')
File runner3 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner3.groovy')
File parallelFeatures = new File(basedir, 'src/test/resources/parallel_features')

assert runner0.exists()
assert runner1.exists()
assert runner2.exists()
assert runner3.exists()

String actualContent0 = ''
String actualContent1 = ''
String actualContent2 = ''
String actualContent3 = ''

int actualNumberOfFeatureFiles = 0
parallelFeatures.eachFileRecurse(FileType.FILES) { featureFile ->
    if (featureFile.name.contains('the-search-for-cheese')) {
        actualContent0 = featureFile.text
    }
    if (featureFile.name.contains('the-search-for-star-wars')) {
        actualContent1 = featureFile.text
    }
    if (featureFile.name.contains('the-search-for-mickey-mouse')) {
        actualContent2 = featureFile.text
    }
    if (featureFile.name.contains('the-search-for-donald-duck')) {
        actualContent3 = featureFile.text
    }
    actualNumberOfFeatureFiles++
}

assert actualNumberOfFeatureFiles == 4

assert actualContent0 == """\
Feature: A Feature File With Tags:  The search for cheese
As a casual user
I want the ability to use Google's search feature
So that I can retrieve useful and accurate information


@test-tag1
Scenario: The search for cheese
Given I am on Google home page
When I enter the keyword of "Cheese"
And click the Submit button
Then the page title returned is "Cheese - Google Search"

"""

assert actualContent1 == """\
Feature: A Feature File With Tags:  The search for star wars
As a casual user
I want the ability to use Google's search feature
So that I can retrieve useful and accurate information


@test-tag2
Scenario: The search for star wars
Given I am on Google home page
When I enter the keyword of "Star Wars"
And click the Submit button
Then the page title returned is "Star Wars - Google Search"

"""

assert actualContent2 == """\
Feature: A Feature File With Tags:  The search for Mickey Mouse
As a casual user
I want the ability to use Google's search feature
So that I can retrieve useful and accurate information


@test-tag2
Scenario: The search for Mickey Mouse
Given I am on Google home page
When I enter the keyword of "Mickey Mouse"
And click the Submit button
Then the page title returned is "Mickey Mouse - Google Search"

"""

assert actualContent3 == """\
Feature: A Feature File With Tags:  The search for Donald Duck
As a casual user
I want the ability to use Google's search feature
So that I can retrieve useful and accurate information


@test-tag1 @test-tag2
Scenario: The search for Donald Duck
Given I am on Google home page
When I enter the keyword of "Donald Duck"
And click the Submit button
Then the page title returned is "Donald Duck - Google Search"

"""
