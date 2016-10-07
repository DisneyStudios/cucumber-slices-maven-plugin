import groovy.io.FileType

File runner0 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner0.groovy')
File parallelFeatures = new File(basedir, 'src/test/resources/parallel_features')

assert runner0.exists()

String actualContent0 = ''

int actualNumberOfFeatureFiles = 0
parallelFeatures.eachFileRecurse(FileType.FILES) { featureFile ->
    if (featureFile.name.contains('the-search-for-star-wars')) {
        actualContent0 = featureFile.text
    }
    actualNumberOfFeatureFiles++
}

assert actualNumberOfFeatureFiles == 1

assert actualContent0 == """\
Feature: Searching Google Using Data Table:  The search for star wars



@regression
Scenario: The search for star wars
Given I am on Google home page
When I enter the keyword of "Star Wars"
And click the Submit button
Then the following page elements shall be displayed
| News |
| Images |
| Videos |

"""
