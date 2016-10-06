
File runner = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner0.groovy')
File logFile = new File(basedir, 'build.log')

String content = logFile.text
assert !runner.exists()
assert content.contains('[WARNING] Skipping feature file [A Feature File Without Scenarios] because it does not contain any scenarios!')
assert content.contains('[WARNING] Missing generated Cucumber Runner files!! Skipping creation of Cucumber Runner files in the src/test/groovy/parallel_runners directory.')