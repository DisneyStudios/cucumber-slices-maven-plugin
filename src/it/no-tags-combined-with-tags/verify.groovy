import groovy.io.FileType

File runner0 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner0.groovy')
File runner1 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner1.groovy')
File runner2 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner2.groovy')
File parallelFeatures = new File(basedir, 'src/test/resources/parallel_features')

assert runner0.exists()
assert runner1.exists()
assert runner2.exists()

String fileContent0 = ''
String fileContent1 = ''
String fileContent2 = ''

parallelFeatures.eachFileRecurse(FileType.FILES) { featureFile ->
    if (featureFile.name.contains('the-search-for-cheese')) {
        fileContent0 = featureFile.text
    }
    if (featureFile.name.contains('the-search-for-star-wars')) {
        fileContent1 = featureFile.text
    }
    if (featureFile.name.contains('the-search-for-avengers')) {
        fileContent2 = featureFile.text
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

assert fileContent2 == """\
Feature: A Feature File Without Tags:  The search for avengers
As a casual user
I want the ability to use Google's search feature
So that I can retrieve useful and accurate information


@test-tag
Scenario: The search for avengers
Given I am on Google home page
When I enter the keyword of "Avengers Age of Ultron"
And click the Submit button
Then the page title returned is "Avengers Age of Ultron - Google Search"

"""