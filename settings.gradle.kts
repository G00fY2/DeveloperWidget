include("app")

pluginManagement {
  repositories {
    google()
    gradlePluginPortal()
  }
  resolutionStrategy {
    eachPlugin {
      if (requested.id.namespace == "com.android") useModule("com.android.tools.build:gradle:${requested.version}")
    }
  }
}

plugins {
  id("com.gradle.enterprise") version "3.5"
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
    publishAlwaysIf(!System.getenv("CI").isNullOrEmpty())
    isUploadInBackground = false
  }
}