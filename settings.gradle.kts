include("app")

pluginManagement {
  repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    jcenter {
      content {
        includeModule("eu.appcom.gradle", "android-versioning")
      }
    }
  }
  resolutionStrategy {
    eachPlugin {
      if (requested.id.namespace == "com.android") {
        useModule("com.android.tools.build:gradle:4.1.0-alpha04")
      }
      if (requested.id.id == "eu.appcom.gradle.android-versioning") {
        useModule("eu.appcom.gradle:android-versioning:1.0.2")
      }
      if (requested.id.namespace == "org.jetbrains.kotlin") {
        useVersion("1.4-M1")
      }
    }
  }
}

plugins {
  id("com.gradle.enterprise") version "3.2"
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
    publishAlwaysIf(!System.getenv("CI").isNullOrEmpty())
  }
}