import groovy.io.FileType

File runner0 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner0.groovy')
File runner1 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner1.groovy')
File parallelFeatures = new File(basedir, 'src/test/resources/parallel_features')

assert runner0.exists()
assert runner1.exists()

String fileContent0 = ''
String fileContent1 = ''

parallelFeatures.eachFileRecurse(FileType.FILES) { featureFile ->
    if (featureFile.name.contains('the-search-for-cheese')) {
        fileContent0 = featureFile.text
    }
    if (featureFile.name.contains('the-search-for-star-wars')) {
        fileContent1 = featureFile.text
    }
}

assert fileContent0 == """\
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

assert fileContent1 == """\
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