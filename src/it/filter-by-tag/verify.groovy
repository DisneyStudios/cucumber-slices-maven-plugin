import groovy.io.FileType

File runner0 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner0.groovy')
File parallelFeatures = new File(basedir, 'src/test/resources/parallel_features')

assert runner0.exists()

String fileContent0 = ''

parallelFeatures.eachFileRecurse(FileType.FILES) { featureFile ->
    if (featureFile.name.contains('the-search-for-cheese')) {
        fileContent0 = featureFile.text
    }
}

assert fileContent0 == """\
Feature: A Feature File With Tags:  The search for cheese
As a casual user
I want the ability to use Google's search feature
So that I can retrieve useful and accurate information


Scenario: The search for cheese
Given I am on Google home page
When I enter the keyword of "Cheese"
And click the Submit button
Then the page title returned is "Cheese - Google Search"

"""