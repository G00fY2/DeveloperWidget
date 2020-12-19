plugins {
  id(Plugins.Android.application)
  id(Plugins.Kotlin.android)
  id(Plugins.Kotlin.kapt)
  id(Plugins.Misc.androidVersioning) version Versions.androidVersioning
}

android {
  compileSdkVersion(Config.androidCompileSdkVersion)
  buildToolsVersion = Versions.buildToolsVersion
  defaultConfig {
    applicationId = Config.applicationId
    minSdkVersion(Config.androidMinSdkVersion)
    targetSdkVersion(Config.androidTargetSdkVersion)
    versionCode = versioning.getVersionCode()
    versionName = versioning.getVersionName()

    resConfigs("en")
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
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
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
    allWarningsAsErrors = true
    jvmTarget = JavaVersion.VERSION_1_8.toString()
    freeCompilerArgs = listOf("-progressive")
    useIR = true
  }
  lintOptions {
    isCheckReleaseBuilds = false // TODO remove when https://issuetracker.google.com/issues/141126614 is fixed
  }
  dependenciesInfo {
    includeInApk = false
  }
  packagingOptions.resources {
    excludes += "DebugProbesKt.bin"
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