import groovy.io.FileType

File runner0 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner0.groovy')
File runner1 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner1.groovy')
File parallelFeatures = new File(basedir, 'src/test/resources/parallel_features')

assert runner0.exists()
assert runner1.exists()

String actualContent0 = ''
String actualContent1 = ''

int actualNumberOfFeatureFiles = 0
parallelFeatures.eachFileRecurse(FileType.FILES) { featureFile ->
    if (featureFile.name.contains('the-search-for-cheese')) {
        actualContent0 = featureFile.text
    }
    if (featureFile.name.contains('the-search-for-star-wars')) {
        actualContent1 = featureFile.text
    }
    actualNumberOfFeatureFiles++
}

assert actualNumberOfFeatureFiles == 2

assert actualContent0 == """\
Feature: A Feature File Without Tags:  The search for cheese
As a casual user
I want the ability to use Google's search feature
So that I can retrieve useful and accurate information


Scenario: The search for cheese
Given I am on Google home page
When I enter the keyword of "Cheese"
And click the Submit button
Then the page title returned is "Cheese - Google Search"

"""

assert actualContent1 == """\
Feature: A Feature File Without Tags:  The search for star wars
As a casual user
I want the ability to use Google's search feature
So that I can retrieve useful and accurate information


Scenario: The search for star wars
Given I am on Google home page
When I enter the keyword of "Star Wars"
And click the Submit button
Then the page title returned is "Star Wars - Google Search"

"""