import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
  id(Plugins.Android.application) version Versions.androidGradle apply false
  kotlin(Plugins.Kotlin.androidGradle) version Versions.kotlin apply false
  id(Plugins.Misc.gradleVersions) version Versions.gradleVersions
}

tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java).configure {
  resolutionStrategy {
    componentSelection {
      all {
        if (Utils.isNonStable(candidate.version) && !Utils.isNonStable(currentVersion)) {
          reject("Release candidate")
        }
      }
    }
  }
}

tasks.register<Delete>("clean") {
  delete(buildDir)
}