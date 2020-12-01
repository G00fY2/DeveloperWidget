plugins {
  id(Plugins.Android.application) version Versions.androidGradle apply false
  kotlin(Plugins.Kotlin.androidGradle) version Versions.kotlin apply false
  id(Plugins.Misc.gradleVersions) version Versions.gradleVersions
}

tasks.register<Delete>("clean") {
  delete(buildDir)
}