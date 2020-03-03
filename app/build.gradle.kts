plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id("eu.appcom.gradle.android-versioning")
}

android {
  compileSdkVersion(29)
  buildToolsVersion = "29.0.3"
  defaultConfig {
    applicationId = "com.g00fy2.developerwidget"
    minSdkVersion(14)
    targetSdkVersion(29)
    versionCode = versioning.getVersionCode()
    versionName = versioning.getVersionName()

    vectorDrawables.useSupportLibrary = true
    setProperty("archivesBaseName", "developerwidget-$versionName-$versionCode")
  }
  signingConfigs {
    create("release") {
      storeFile = file("../keystore.jks")
      storePassword = findProperty("my_storepass") as String? ?: ""
      keyAlias = findProperty("my_keyalias") as String? ?: ""
      keyPassword = findProperty("my_keypass") as String? ?: ""
      isV1SigningEnabled = true
      isV2SigningEnabled = true
    }
  }
  buildTypes {
    getByName("debug") {
      applicationIdSuffix = ".debug"
    }
    getByName("release") {
      signingConfig = signingConfigs.getByName("release")
      isShrinkResources = true
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  buildFeatures {
    viewBinding = true
  }
  sourceSets {
    getByName("main").java.srcDirs("src/main/kotlin")
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

repositories {
  google()
  mavenCentral()
  jcenter {
    content {
      includeModule("com.g00fy2", "versioncompare")
      includeModule("org.jetbrains.trove4j", "trove4j") // required by com.android.tools.lint:lint-gradle
    }
  }
}
dependencies {
  // Kotlin
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.70")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.3")

  // AndroidX
  implementation("androidx.appcompat:appcompat:1.2.0-alpha02")
  implementation("androidx.core:core-ktx:1.3.0-alpha01")
  implementation("androidx.activity:activity:1.1.0")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")
  implementation("androidx.recyclerview:recyclerview:1.2.0-alpha01")
  implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta4")
  implementation("androidx.vectordrawable:vectordrawable:1.1.0")

  // UI
  implementation("com.google.android.material:material:1.2.0-alpha05")

  // Misc
  implementation("com.jakewharton.timber:timber:4.7.1")
  implementation("com.g00fy2:versioncompare:1.3.4")

  // Dagger
  implementation("com.google.dagger:dagger:2.26")
  kapt("com.google.dagger:dagger-compiler:2.26")
  implementation("com.google.dagger:dagger-android:2.26")
  implementation("com.google.dagger:dagger-android-support:2.26")
  kapt("com.google.dagger:dagger-android-processor:2.26")
}