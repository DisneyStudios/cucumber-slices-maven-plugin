package com.disney.studio.cucumber.slices.plugin.assemble

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import java.nio.file.Paths
import static com.disney.studio.cucumber.slices.plugin.common.SystemUtilities.*


@Slf4j
class FeatureFileAssembler {
    private JsonSlurper jsonSlurper
    private String featureFileName
    private String featureFilePath
    private List<String> featureInformation = []
    private List<String> scenarioInformation = []
    private List<String> expectedTags = []
    private List<String> actualTags = []
    private List<String> backgroundSteps = []
    private List<String> generalSteps = []
    private Map<Integer, List<String>> examples = [:]
    private String timestamp
    private boolean doesParallelFeatureDirExist
    private FeatureFileWriter featureFileWriter

    /**
     * Construct a FeatureFileAssembler using the specified <em>expectedTags</em>
     * @param expectedTags
     * The Cucumber tags that one would expect to filter on when creating the feature files
     */
    FeatureFileAssembler(List<String> expectedTags) {
        if (expectedTags.size() > 0) {
            boolean isTagFormattedCorrectly = expectedTags.every { it.contains('@') }
            assert isTagFormattedCorrectly, "Missing '@' character!!! One or more of the supplied Cucumber tags, '$expectedTags', is not properly formatted in the POM file"
        }

        this.expectedTags = expectedTags
        this.jsonSlurper = new JsonSlurper()
        this.featureFileWriter = new FeatureFileWriter()
        log.debug("Supplied list of tag filters: $expectedTags")
    }

    FeatureFileWriter getFeatureFileWriter() {
        return featureFileWriter
    }

    /**
     * Assemble the feature file using the specified JSON. The contents of the JSON represents the <b>entire</b>
     * feature file.
     * @param json
     * The supplied JSON used to assemble a feature file based on Gherkin
     * syntax -- the JSON format is assumed to meet the required Cucumber format and <b>must</b> be taken from
     * valid Gherkin.
     */
    void assembleFeatureFileFromJson(String json) {
        assert json, log.warn('Missing JSON structure!! Cannot proceed to assemble the feature file.')
        clearAllDataStructures()
        def parsedJson = objectifyJson(json)
        extractFeatureFilePathFromParsedJson(parsedJson)
        transformParsedJsonToGherkin(parsedJson)
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private String getTimestamp() {
        return new Date().format('hhmmss-S')
    }

    private void clearAllDataStructures() {
        if (backgroundSteps) {
            log.debug('Clearing Cucumber Background data structure.')
            backgroundSteps.clear()
        }
        if (actualTags) {
            log.debug('Clearing Cucumber tags data structure.')
            actualTags.clear()
        }
        if (generalSteps) {
            log.debug('Clearing Cucumber general step data structure.')
            generalSteps.clear()
        }
        if (featureInformation) {
            log.debug('Clearing Cucumber feature data structure.')
            featureInformation.clear()
        }
        if (scenarioInformation) {
            log.debug('Clearing Cucumber scenario data structure.')
            scenarioInformation.clear()
        }
        if (examples) {
            log.debug('Clearing Cucumber scenario outline data structure.')
            examples.clear()
        }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private void clearSuppliedDataStructures(List<List<String>> dataStructures) {
        for (List dataStructure in dataStructures) {
            dataStructure.clear()
        }
    }

    private Object objectifyJson(String json) {
        log.debug('Objectifying the supplied JSON text...')
        def parsedJson = jsonSlurper.parseText(json)
        log.debug('Successfully objectified the supplied JSON text.')
        return parsedJson
    }

    private void extractFeatureFilePathFromParsedJson(Object parsedJson) {
        if (parsedJson instanceof List) parsedJson = parsedJson[0]
        featureFilePath = Paths.get(parsedJson.uri).parent.toString()
    }

    private void transformParsedJsonToGherkin(Object parsedJson) {
        if (parsedJson instanceof List) parsedJson = parsedJson[0]
        // do we have a collection of elements defined in the _parsed_ JSON
        // data structure. A collection of elements is composed of Gherkin steps and/or
        // Gherkin examples
        if (isGherkinElementCollectionDefined(parsedJson)) {
            // process _each_ element within the JSON data structure
            for (Map element in parsedJson.elements) {
                // do we have an element with an ID
                if (isElementIdDefined(element)) {
                    log.debug("Processing Feature file's Scenario...")
                    if (isExampleCollectionDefined(element)) {
                        createFeatureFromScenarioOutline(element, parsedJson)
                    } else {
                        createFeatureFromScenario(element, parsedJson)
                    }
                } else {
                    processElement(element)
                    log.debug("'${backgroundSteps[0]}', successfully processed.")
                }
            }
        } else {
            log.warn("Skipping feature file [${parsedJson.name}] because it does not contain any scenarios!")
        }
    }

    /**
     * Let's create a feature file based on input from a <em>Scenario Outline</em>.
     * @param element
     * The JSON element that contains information associated with the Scenario Outline examples and steps
     * @param parsedJson
     * The <b>entire</b> JSON structure representing the full contents of the parsed Gherkin feature file
     */
    private void createFeatureFromScenarioOutline(Map element, Object parsedJson) {
        log.debug('Processing Scenario Outline section(s)...')
        // log the name of the scenario to standard out and log file
        logScenarioName(element)
        // construct the example data structure obtained from the Scenario Outline
        processExamples(element)

        // now, process each scenario outline example, converting the example into
        // a single scenario, contained within a feature file
        int numOfExamples = examples.size()
        for (int e = 0; e < numOfExamples; e++) {
            timestamp = getTimestamp()
            // extract scenario steps
            processElement(element)
            // setup the Feature's narrative
            processFeatureNarrative(parsedJson)

            if (expectedTags.size() > 0) {
                // write out the information contained within the collected data structures to a Feature file
                if (isScenarioTagged()) {
                    writeFeatureFile()
                } else {
                    log.warn("Skipping further processing because the Scenario's tags, $actualTags, do not match the supplied tag filter, $expectedTags!")
                }
            } else {
                writeFeatureFile()
            }

            // clear out the data structures, except for the background steps ... the background steps get cleared out upon
            // reading in a NEW feature file
            clearSuppliedDataStructures([featureInformation, actualTags, generalSteps])
            // CRITICAL ... remove the first element from the 'example' data structure and reassign
            // to itself.  In essence, we are pruning the example collection each time through this -- for loop --
            // This ensures we do not reuse the same example scenario outline the next time through the loop
            examples = examples.drop(1)
        }
    }

    /**
     * Let's create a feature file based on input from a <em>Scenario</em>.
     * @param element
     * The JSON element that contains information associated with the Scenario steps
     * @param parsedJson
     * The <b>entire</b> JSON structure, representing the full contents of the parsed Gherkin feature file
     */
    private void createFeatureFromScenario(Map element, Object parsedJson) {
        timestamp = getTimestamp()
        // log the name of the scenario to standard out and log file
        logScenarioName(element)
        // extract scenario steps
        processElement(element)
        // setup the Feature's narrative
        processFeatureNarrative(parsedJson)

        if (expectedTags.size() > 0) {
            // write out the information contained within the collected data structures to a Feature file
            if (isScenarioTagged()) {
                writeFeatureFile()
            } else {
                log.warn("Skipping further processing because the Scenario's tags, $actualTags, do not match the supplied tag filter, $expectedTags!")
            }
        } else {
            writeFeatureFile()
        }

        // clear out the data structures, except for the background steps ... the background steps get cleared out upon
        // reading in a NEW feature file
        clearSuppliedDataStructures([featureInformation, actualTags, generalSteps])
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private void logScenarioName(Map element) {
        log.debug("Processing the following scenario... '${element.name}'")
    }

    /**
     * Process the Scenario Outline examples and store the resulting information in a Map data structure. The
     * resulting data structure should resemble
     * <pre>
     *     0:['first name::Robert', 'last name::Smith']
     *     1:['first name::Jenny', 'last name::Sorenson']
     * </pre>
     * A Map whose values are a list of strings that represent the <b>rows</b> of the Scenario Outline examples. The
     * above data structure would look like the following in Gherkin syntax
     * <pre>
     *     Examples: Some description
     *      | first name   | last name  |
     *      | Robert       | Smith      |
     *      | Jenny        | Sorenson   |
     * </pre>
     * @param element
     * The JSON element that contains information associated with the Scenario Outline examples and steps
     */
    private void processExamples(Map element) {
        int rowCounter = 0

        for (exampleSectionCollection in element.examples.rows.cells) {
            int exampleSectionCounter = 0
            List<String> headers = []
            for (List<String> cells in exampleSectionCollection) {
                if (exampleSectionCounter == 0) {
                    // add each cell to the HEADERS collection
                    cells.each { headers.add(it) }
                } else {
                    List<String> celldata = []
                    for (int c = 0; c < cells.size(); c++) {
                        celldata.add("${headers[c]}::${cells[c]}")
                    }
                    examples.put(rowCounter, celldata)
                }

                // increment the row counter if and only if the example section counter is non-zero. in other words,
                // we are not working with a row associated with the HEADERS
                if (exampleSectionCounter) { rowCounter++ }
                exampleSectionCounter++
            }
        }

    }

    private void processFeatureNarrative(Object parsedJson) {
        def scenarioName = (actualTags.isEmpty()) ? generalSteps[0] : generalSteps[1]

        // store information specific to the feature like its
        //   Keyword (e.g., 'Feature')
        //   Name (e.g., 'The feature title/summary')
        //   Description
        featureInformation.add("${parsedJson.keyword}: ${parsedJson.name}: ${scenarioName?.split(':')[1]}")
        featureInformation.add(parsedJson.description)
    }

    /**
     * Process the Scenario steps using the specified JSON element.  Processing the element involves
     * <ol>
     *     <li>extracting the feature file name from the element's information
     *     <li>extracting the element's tags
     *     <li>extracting the element's scenario
     *     <li>extracting the element's scenario steps
     * </ol>
     * @param element
     * The JSON element that contains information associated with the Scenario
     */
    private void processElement(Map element) {
        if (isBackgroundDefined(element)) log.debug('The supplied feature file contains a Cucumber Background section.')
        // store feature file name
        if (isElementIdDefined(element)) {
            // the name of the feature file on disk
            featureFileName = "${element.id.split(';')[1]}-${timestamp}.feature"

            if (element.type == 'scenario_outline') {
                featureFileName = findAndReplaceOutlineParameters(featureFileName)
                featureFileName = featureFileName.replaceAll(/\s+/, '-')
            }

            // process scenario tags
            if (element.tags) {
                // add tags as a comma-separated list
                generalSteps.add((element.tags.name as List).join(' '))
                // also assign to actual tags data structure
                actualTags.add(generalSteps.last())
            }

            // logic to support Scenario Outlines
            def scenarioKeyword
            def scenarioName
            if (element.type == 'scenario_outline') {
                scenarioName = findAndReplaceOutlineParameters(element.name)
                scenarioKeyword = 'Scenario'
            } else {
                scenarioName = element.name
                scenarioKeyword = element.keyword
            }

            // the scenario's NAME
            generalSteps.add("$scenarioKeyword: $scenarioName")
        }

        extractSteps(element)
    }

    private void extractSteps(Map element) {
        for (Map step in element.steps) {
            extractStep(element, step)
            if (isStepAssociatedWithDataTable(step)) { extractDataTable(step) }
            if (element.type == 'scenario_outline') {
                def newStep = findAndReplaceOutlineParameters(generalSteps.last())
                if (newStep) {
                    generalSteps.pop()
                    generalSteps.push(newStep)
                }
            }
        }
    }

    private void extractStep(Map element, Map step) {
        def gherkinStep
        if (element.type == 'background'){
            gherkinStep = step.keyword + step.name
            backgroundSteps.add(gherkinStep)
        } else {
            gherkinStep = step.keyword + step.name
            generalSteps.add(gherkinStep)
        }
    }

    private void extractDataTable(Map step) {
        def gherkinStep
        for (List cell in step.rows.cells) {
            gherkinStep = '|'
            for (String item in cell) {
                gherkinStep += " $item "
                gherkinStep += '|'
            }
            generalSteps.add(gherkinStep)
        }
    }

    /**
     * Finds each of the Scenario Outline parameters and replaces the parameter with
     * the appropriate value contained in the outline's list of Examples
     * @param step
     * The Gherkin step with the Scenario Outline parameter
     * @return
     * The Gherkin step with the Scenario Outline parameter replaced by the actual value
     */
    private String findAndReplaceOutlineParameters(String step) {
        def newStepStatement = ''

        // loop over the data structure that represents the Scenario Outline examples
        for (example in examples) {
            for (exampleValue in example.value) {
                def scenarioOutlineParameter = exampleValue.split('::')[0] // store the outline parameter
                def replacement = exampleValue.split('::')[1] // store the ACTUAL value of the example

                // replace the special regex meta character $ with \$
                if (replacement.contains('$')) {
                    replacement = findAndReplaceMetacharacters(replacement)
                }

                // are the words in the step surrounded by whitespace characters...if the answer is no, then replace all
                // whitespace with hyphens within the extracted outline parameter.
                //
                // For instance, lets say you had a
                //     feature file name = 'the-wonderful-world-of-<world-name>.feature'
                // and a
                //     scenarioOutlineParameter = 'world name'
                //
                // after execution of this statement, then scenarioOutlineParameter = 'world-name'
                if (!(step =~ /(\s\w+\s)+/)) {
                    scenarioOutlineParameter = scenarioOutlineParameter.replaceAll(/\s+/, '-')
                }

                // check for matches and replace the parameter with the ACTUAL value
                if (step =~ /\<$scenarioOutlineParameter\>/) {
                    newStepStatement = (step =~ /\<$scenarioOutlineParameter\>/).replaceFirst(replacement)
                }

                // Reassign the step, which is passed into the method, with the NEW step, if and only if the NEW step exists.
                // This ensures that steps which read like
                //
                //    "When I enter <first number> + <second number>"
                //
                //    Example: adding
                //    | first number | second number |
                //    | 2            | 3             |
                //
                // will have each of the outline parameters filled with the actual values, (i.e, "When I enter 2 + 3").
                //
                // Without this logic, the end result would read like "When I enter <first number> + 3".
                if (newStepStatement) {
                    step = newStepStatement
                }
            }
            // break out of the outer loop if the NEW step exists
            if (newStepStatement) break
        }

        return (newStepStatement.isEmpty()) ? step : newStepStatement
    }

    static String findAndReplaceDoubleQuotesWithEmptyString(String string) {
        return string.replaceAll('"', '')
    }

    static String findAndReplaceDollarWithEmptyString(String string) {
        return string.replaceAll('\\$', '')
    }

    static String findAndReplaceMetacharacters(String string) {
        return string.replaceAll('\\$', '\\\\\\$')
    }

    private void writeFeatureFile() {
        assert featureFileName, "Cannot create feature file. The supplied feature file name '$featureFileName' is undefined!!"
        def parallelFeaturesDirectory = removeSubDirectoriesFromFeaturesPath(featureFilePath)
        parallelFeaturesDirectory = parallelFeaturesDirectory.replace('features', 'parallel_features')

        featureFileName = findAndReplaceDoubleQuotesWithEmptyString(featureFileName)
        featureFileName = findAndReplaceDollarWithEmptyString(featureFileName)

        File dir = new File(parallelFeaturesDirectory)
        log.debug("Parallel features directory: ${dir.absolutePath}")

        // delete the directory and then re-create the directory
        if (!doesParallelFeatureDirExist) {
            dir.deleteDir()
            dir.mkdirs()
            doesParallelFeatureDirExist = true
        }

        def featureFilePath = "${parallelFeaturesDirectory}" + FILESEP + "${featureFileName}"
        File featureFile = new File(featureFilePath)

        log.debug("Feature file: ${featureFile.absolutePath}")

        // Delete the file if it exists
        if (featureFile.exists()) featureFile.delete()

        featureFileWriter.file = featureFile
        featureFileWriter.featureFiles.add(featureFile)
        featureFileWriter.writeFeature([featureInformation, backgroundSteps, generalSteps])

        def message
        if (actualTags) {
            message = "'${generalSteps[1]}', sucessfully processed and written to '$featureFileName'."
        } else {
            message = "'${generalSteps[0]}', sucessfully processed and written to '$featureFileName'."
        }
        log.debug(message)
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private String removeSubDirectoriesFromFeaturesPath(String path) {
//        List tokenizedPathElements

//        if (System.getProperty('os.name').contains('Windows')) {
        List tokenizedPathElements = path.tokenize(FILESEP)
//        } else {
//            tokenizedPathElements = path.tokenize('/')
//        }

        int indexOfFeaturesPlusOne = tokenizedPathElements.indexOf('features') + 1
        int numOfElementsToDrop = tokenizedPathElements.size() - indexOfFeaturesPlusOne
        tokenizedPathElements = tokenizedPathElements.dropRight(numOfElementsToDrop)

        // reassemble path elements to form an actual path
        if (!OS.startsWith('win')) {
            return FILESEP + "${tokenizedPathElements.join(FILESEP)}"
        }

        return "${tokenizedPathElements.join(FILESEP)}"
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private boolean isGherkinElementCollectionDefined(Object parsedJson) {
        return parsedJson.elements?.steps
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private boolean isExampleCollectionDefined(Map element) {
        return element?.examples
    }

    private boolean isBackgroundDefined(Map element) {
        if (element.type == 'background') {
            backgroundSteps.add("${element.keyword}: ${element.name}")
            return true
        }
        return false
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private boolean isElementIdDefined(Map element) {
        if (element.id) {
            return true
        }
        return false
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private boolean isStepAssociatedWithDataTable(Map step) {
        if (step.rows) { return true }
        return false
    }

    /**
     * Do any of the supplied <b>expected</b> tags match the <em>actual</em> tag values obtained
     * from the Cucumber scenarios
     * @return
     * <code>true</code> if there is a match, otherwise <code>false</code>
     */
    private boolean isScenarioTagged() {
        if (actualTags) {
            return expectedTags.any { expectedTag ->
                actualTags[0] =~ /$expectedTag/
            }
        }
        return false
    }
}
