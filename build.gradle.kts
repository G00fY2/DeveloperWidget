plugins {
  id(Plugins.Android.application) version Versions.androidGradle apply false
  kotlin(Plugins.Kotlin.androidGradle) version Versions.kotlin apply false
  id(Plugins.Misc.gradleVersions) version Versions.gradleVersions
}

tasks.dependencyUpdates.configure {
  rejectVersionIf { Versions.maturityLevel(candidate.version) < Versions.maturityLevel(currentVersion) }
}

tasks.register<Delete>("clean") {
  delete(buildDir)
}