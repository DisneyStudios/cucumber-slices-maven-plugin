import groovy.io.FileType

File runner0 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner0.groovy')
File runner1 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner1.groovy')
File runner2 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner2.groovy')
File runner3 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner2.groovy')
File runner4 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner2.groovy')
File runner5 = new File(basedir, 'src/test/groovy/parallel_runners/ParallelRunner2.groovy')

assert runner0.exists()
assert runner1.exists()
assert runner2.exists()
assert runner3.exists()
assert runner4.exists()
assert runner5.exists()
