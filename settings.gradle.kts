include("app")

pluginManagement {
  val kotlinVersion: String by settings
  val androidGradlePluginVersion: String by settings
  val gradleEnterpriseVersion: String by settings
  repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
    jcenter {
      content {
        includeModule("eu.nanogiants", "android-versioning")
      }
    }
  }
  resolutionStrategy {
    eachPlugin {
      when (requested.id.id) {
        "com.android.application" -> useModule("com.android.tools.build:gradle:$androidGradlePluginVersion")
        "de.nanogiants.android-versioning" -> useModule("de.nanogiants:android-versioning:2.3.0")
        "com.gradle.enterprise" -> useVersion(gradleEnterpriseVersion)
      }
      if (requested.id.namespace == "org.jetbrains.kotlin") useVersion(kotlinVersion)
    }
  }
}

plugins {
  id("com.gradle.enterprise")
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
    publishAlwaysIf(!System.getenv("CI").isNullOrEmpty())
    isUploadInBackground = false
  }
}