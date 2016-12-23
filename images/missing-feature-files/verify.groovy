
File logFile = new File(basedir, 'build.log')

String content = logFile.text
assert content.contains('[INFO] Total number of feature files scanned: 0')
assert content.contains('[WARNING] Missing feature files!! Skipping further processing.')