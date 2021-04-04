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

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    jcenter {
      content {
        includeModule("org.jetbrains.trove4j", "trove4j")
      }
    }
  }
}

plugins {
  id("com.gradle.enterprise") version "3.6"
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
    publishAlwaysIf(!System.getenv("CI").isNullOrEmpty())
    isUploadInBackground = false
  }
}