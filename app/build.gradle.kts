plugins {
  id(Plugins.Android.application)
  id(Plugins.Kotlin.android)
  id(Plugins.Kotlin.kapt)
}

android {
  compileSdk = Versions.androidCompileSdk
  buildToolsVersion = Versions.androidBuildTools
  defaultConfig {
    applicationId = "com.g00fy2.developerwidget"
    minSdk = Versions.androidMinSdk
    targetSdk = Versions.androidTargetSdk
    versionName = GitUtils.gitTag
    versionCode = GitUtils.gitCommitCount

    resourceConfigurations.add("en")
    vectorDrawables.useSupportLibrary = true
    setProperty("archivesBaseName", "developerwidget-$versionName-$versionCode")
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
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    allWarningsAsErrors = true
    jvmTarget = JavaVersion.VERSION_11.toString()
    freeCompilerArgs = freeCompilerArgs + listOf("-progressive", "-opt-in=kotlin.RequiresOptIn")
  }
  dependenciesInfo {
    includeInApk = false
  }
  packagingOptions.resources {
    excludes += listOf("DebugProbesKt.bin", "**/*.kotlin_*", "kotlin-tooling-metadata.json")
  }
}

dependencies {
  implementation(Deps.Kotlin.coroutines)

  implementation(Deps.AndroidX.appcompat)
  implementation(Deps.AndroidX.core)
  implementation(Deps.AndroidX.activity)
  implementation(Deps.AndroidX.fragment)
  implementation(Deps.AndroidX.lifecycle)
  implementation(Deps.AndroidX.recyclerView)
  implementation(Deps.AndroidX.constraintLayout)
  implementation(Deps.AndroidX.vectorDrawable)

  implementation(Deps.UI.materialDesign)

  implementation(Deps.Misc.timber)
  implementation(Deps.Misc.versionCompare)

  implementation(Deps.Dagger.dagger)
  kapt(Deps.Dagger.daggerCompiler)
  implementation(Deps.Dagger.daggerAndroid)
  implementation(Deps.Dagger.daggerAndroidSupport)
  kapt(Deps.Dagger.daggerAndroidProcessor)
}