
File logFile = new File(basedir, 'build.log')

String content = logFile.text
assert content.contains("Missing '@' character!!! One or more of the supplied Cucumber tags, '[test-tag]', is not properly formatted in the POM file")
