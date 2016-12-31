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
    if (featureFile.name.contains('adding-2-numbers')) {
        actualContent0 = featureFile.text
    }
    if (featureFile.name.contains('adding-3-numbers')) {
        actualContent1 = featureFile.text
    }

    actualNumberOfFeatureFiles++
}

assert actualNumberOfFeatureFiles == 2

assert actualContent0 == """\
Feature: Addition:  Adding 2 numbers



@regression
Scenario: Adding 2 numbers
Given I have 2 numbers
When I enter 2 + 3
Then the result is 5

"""

assert actualContent1 == """\
Feature: Addition:  Adding 3 numbers



@regression
Scenario: Adding 3 numbers
Given I have 3 numbers
When I enter 2 + 3 + 5
Then the result is 10

"""

