object Versions {

  const val androidMinSdk = 14
  const val androidCompileSdk = 29
  const val androidTargetSdk = 29

  const val androidBuildTools = "31.0.0"
  const val androidGradle = "7.0.1"
  const val kotlin = "1.5.21"

  const val gradleVersions = "0.39.0"
  const val androidVersioning = "2.4.0"

  const val coroutines = "1.5.1"

  const val appcompat = "1.3.1"
  const val core = "1.6.0"
  const val activity = "1.2.4"
  const val fragment = "1.3.6"
  const val lifecycle = "2.3.1"
  const val recyclerView = "1.2.1"
  const val constraintLayout = "2.1.0"
  const val vectorDrawable = "1.1.0"

  const val materialDesign = "1.4.0"

  const val timber = "5.0.1"
  const val versionCompare = "1.4.1"

  const val dagger = "2.38.1"

  fun maturityLevel(version: String): Int {
    val levels = listOf("alpha", "beta", "m", "rc")
    levels.forEachIndexed { index, s ->
      if (version.matches(".*[.\\-]$s[.\\-\\d]*".toRegex(RegexOption.IGNORE_CASE))) return index
    }
    return levels.size
  }
}