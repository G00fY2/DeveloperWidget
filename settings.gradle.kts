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
        includeModule("eu.appcom.gradle", "android-versioning")
      }
    }
  }
  resolutionStrategy {
    eachPlugin {
      when (requested.id.id) {
        "com.android.application" -> useModule("com.android.tools.build:gradle:$androidGradlePluginVersion")
        "eu.appcom.gradle.android-versioning" -> useModule("eu.appcom.gradle:android-versioning:1.0.2")
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