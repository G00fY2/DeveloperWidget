object Versions {

  const val androidGradle = "7.1.0-alpha02"
  const val buildToolsVersion = "31.0.0-rc5"
  const val kotlin = "1.5.20"

  const val gradleVersions = "0.39.0"
  const val androidVersioning = "2.4.0"

  const val coroutines = "1.5.0"

  const val appcompat = "1.3.0"
  const val core = "1.6.0"
  const val activity = "1.3.0-beta02"
  const val fragment = "1.3.5"
  const val lifecycle = "2.4.0-alpha02"
  const val recyclerView = "1.2.1"
  const val constraintLayout = "2.1.0-beta02"
  const val vectorDrawable = "1.2.0-alpha02"

  const val materialDesign = "1.4.0-rc01"

  const val timber = "4.7.1"
  const val versionCompare = "1.4.1"

  const val dagger = "2.37"

  fun maturityLevel(version: String): Int {
    val levels = listOf("alpha", "beta", "m", "rc")
    levels.forEachIndexed { index, s ->
      if (version.matches(".*[.\\-]$s[.\\-\\d]*".toRegex(RegexOption.IGNORE_CASE))) return index
    }
    return levels.size
  }
}