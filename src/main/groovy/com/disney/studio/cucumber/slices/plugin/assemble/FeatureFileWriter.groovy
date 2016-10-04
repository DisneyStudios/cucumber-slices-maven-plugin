package com.disney.studio.cucumber.slices.plugin.assemble

class FeatureFileWriter {
    private static final String EOL = System.getProperty('line.separator')
    File file
    List<File> featureFiles = []

    void writeFeature(List dataStructureCollection) {
        if (!file) {
            throw new IllegalArgumentException("Undefined path to feature file!!")
        }
        for (dataStructure in dataStructureCollection) {
            appendDataToFile(dataStructure)
            file.withWriterAppend { writer -> writer.write(EOL)}
        }
    }

    private void appendDataToFile(dataStructure) {
        for (data in dataStructure) {
            file.withWriterAppend { writer ->
                writer.write(data.toString() + EOL)
            }
        }
    }

}
