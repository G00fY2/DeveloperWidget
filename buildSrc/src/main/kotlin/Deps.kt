object Deps {

  object AndroidX {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val core = "androidx.core:core-ktx:${Versions.core}"
    const val activity = "androidx.activity:activity:${Versions.activity}"
    const val fragment = "androidx.fragment:fragment:${Versions.fragment}"
    const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val vectorDrawable = "androidx.vectordrawable:vectordrawable:${Versions.vectorDrawable}"
  }

  object Kotlin {
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
  }

  object UI {
    const val materialDesign = "com.google.android.material:material:${Versions.materialDesign}"
  }

  object Misc {
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val versionCompare = "io.github.g00fy2:versioncompare:${Versions.versionCompare}"
  }

  object Dagger {
    const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    const val daggerAndroid = "com.google.dagger:dagger-android:${Versions.dagger}"
    const val daggerAndroidSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
    const val daggerAndroidProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
  }
}