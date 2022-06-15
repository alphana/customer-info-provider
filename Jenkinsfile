rallyBuildPipeline()
.inParallel(
//FIXME: add sonarqube here!
	run.gradle("build"))
.inSeries(
//FIXME: do we need to publish on feature branches?
	run.onBranchOnly('master').gradle("publish"))
.run()