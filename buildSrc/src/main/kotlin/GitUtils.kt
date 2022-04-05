import java.io.IOException
import java.util.concurrent.TimeUnit

object GitUtils {

  val gitTag: String by lazy {
    listOf("git", "describe", "--tags", "--abbrev=0").runCmd().also { println("VersionName $it") }
  }

  val gitCommitCount: Int by lazy {
    listOf("git", "rev-list", "--count", "HEAD").runCmd().toInt().also { println("VersionCode $it") }
  }

  private fun List<String>.runCmd(): String {
    try {
      ProcessBuilder(this).redirectErrorStream(true).start().run {
        waitFor(10, TimeUnit.SECONDS)
        val outputMessage = inputStream.bufferedReader().use { it.readText().trim() }
        if (exitValue() == 0) {
          return outputMessage
        } else {
          throw IOException(outputMessage)
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
      throw e
    }
  }
}