package com.disney.studio.cucumber.slices.plugin.disassemble

import gherkin.formatter.JSONFormatter
import gherkin.parser.Parser
import gherkin.util.FixJava
import groovy.util.logging.Slf4j

@Slf4j
class FeatureFileParser {

    @SuppressWarnings("GrMethodMayBeStatic")
    String formatAsJson(String fileName) {
        log.info("Converting feature file to JSON: $fileName...")

        String gherkin = FixJava.readReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"))
        StringBuilder json = new StringBuilder()
        JSONFormatter formatter = new JSONFormatter(json)
        Parser parser = new Parser(formatter)
        parser.parse(gherkin, fileName, 0)
        formatter.done()
        formatter.close()

        log.info('Successfully converted feature file to JSON.')

        return json.toString()
    }
}
