plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id("de.nanogiants.android-versioning")
}

android {
  compileSdkVersion(30)
  buildToolsVersion = "30.0.1"
  defaultConfig {
    applicationId = "com.g00fy2.developerwidget"
    minSdkVersion(14)
    targetSdkVersion(30)
    versionCode = versioning.getVersionCode()
    versionName = versioning.getVersionName()

    vectorDrawables.useSupportLibrary = true
    setProperty("archivesBaseName", "developerwidget")
  }
  signingConfigs {
    create("release") {
      storeFile = file("../keystore.jks")
      storePassword = findProperty("my_storepass") as String?
      keyAlias = findProperty("my_keyalias") as String?
      keyPassword = findProperty("my_keypass") as String?
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
    aidl = false
    renderScript = false
    resValues = false
    shaders = false
  }
  dependenciesInfo {
    includeInApk = false
    includeInBundle = true
  }
  sourceSets {
    getByName("main").java.srcDirs("src/main/kotlin")
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }
  lintOptions {
    // TODO remove when https://issuetracker.google.com/issues/141126614 is fixed
    isCheckReleaseBuilds = false
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
  maven ("https://dl.bintray.com/kotlin/kotlin-eap")
  maven("https://kotlin.bintray.com/kotlinx")
}
dependencies {
  // Kotlin
  implementation(kotlin("stdlib-jdk7"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.8-1.4.0-rc")

  // AndroidX
  implementation("androidx.appcompat:appcompat:1.3.0-alpha01")
  implementation("androidx.core:core-ktx:1.5.0-alpha01")
  implementation("androidx.activity:activity:1.2.0-alpha07")
  implementation("androidx.fragment:fragment:1.3.0-alpha07")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.0-alpha06")
  implementation("androidx.recyclerview:recyclerview:1.2.0-alpha05")
  implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta8")
  implementation("androidx.vectordrawable:vectordrawable:1.2.0-alpha01")

  // UI
  implementation("com.google.android.material:material:1.3.0-alpha02")

  // Misc
  implementation("com.jakewharton.timber:timber:4.7.1")
  implementation("com.g00fy2:versioncompare:1.3.4")

  // Dagger
  implementation("com.google.dagger:dagger:2.28.3")
  kapt("com.google.dagger:dagger-compiler:2.28.3")
  implementation("com.google.dagger:dagger-android:2.28.3")
  implementation("com.google.dagger:dagger-android-support:2.28.3")
  kapt("com.google.dagger:dagger-android-processor:2.28.3")
}