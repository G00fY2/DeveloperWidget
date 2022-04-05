object Versions {

  const val androidMinSdk = 14
  const val androidCompileSdk = 31
  const val androidTargetSdk = 29

  const val androidBuildTools = "32.0.0"
  const val androidGradle = "7.1.2"
  const val kotlin = "1.6.20"

  const val gradleVersions = "0.42.0"
  const val androidVersioning = "2.4.0"

  const val coroutines = "1.6.1"

  const val appcompat = "1.4.1"
  const val core = "1.7.0"
  const val activity = "1.4.0"
  const val fragment = "1.4.1"
  const val lifecycle = "2.4.1"
  const val recyclerView = "1.2.1"
  const val constraintLayout = "2.1.3"
  const val vectorDrawable = "1.1.0"

  const val materialDesign = "1.5.0"

  const val timber = "5.0.1"
  const val versionCompare = "1.5.0"

  const val dagger = "2.41"

  private val sortedReleaseQualifiers = listOf("alpha", "beta", "m", "rc")

  fun maturityLevel(version: String): Int {
    val index = sortedReleaseQualifiers.indexOfFirst {
      version.matches(".*[.\\-]$it[.\\-\\d]*".toRegex(RegexOption.IGNORE_CASE))
    }
    return if (index < 0) sortedReleaseQualifiers.size else index
  }
}