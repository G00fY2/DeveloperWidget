plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id("de.nanogiants.android-versioning")
}

android {
  compileSdkVersion(29)
  buildToolsVersion = "30.0.2"
  defaultConfig {
    applicationId = "com.g00fy2.developerwidget"
    minSdkVersion(14)
    targetSdkVersion(29)
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
  sourceSets {
    getByName("main").java.srcDirs("src/main/kotlin")
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
    useIR = true
  }
  lintOptions {
    isCheckReleaseBuilds = false // TODO remove when https://issuetracker.google.com/issues/141126614 is fixed
  }
  dependenciesInfo {
    includeInApk = false
  }
}

versioning {
  keepOriginalMappingFile = false
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
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

  // AndroidX
  implementation("androidx.appcompat:appcompat:1.3.0-alpha02")
  implementation("androidx.core:core-ktx:1.5.0-alpha04")
  implementation("androidx.activity:activity:1.2.0-beta01")
  implementation("androidx.fragment:fragment:1.3.0-beta01")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.0-beta01")
  implementation("androidx.recyclerview:recyclerview:1.2.0-alpha06")
  implementation("androidx.constraintlayout:constraintlayout:2.0.2")
  implementation("androidx.vectordrawable:vectordrawable:1.2.0-alpha02")

  // UI
  implementation("com.google.android.material:material:1.3.0-alpha03")

  // Misc
  implementation("com.jakewharton.timber:timber:4.7.1")
  implementation("com.g00fy2:versioncompare:1.3.5")

  // Dagger
  implementation("com.google.dagger:dagger:2.29.1")
  kapt("com.google.dagger:dagger-compiler:2.29.1")
  implementation("com.google.dagger:dagger-android:2.29.1")
  implementation("com.google.dagger:dagger-android-support:2.29.1")
  kapt("com.google.dagger:dagger-android-processor:2.29.1")
}