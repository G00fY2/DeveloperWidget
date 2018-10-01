package de.g00fy2.developerwidget.web

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import de.g00fy2.developerwidget.web.model.Release
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class GithubProjectInfo(private var client: OkHttpClient) {

  private val adapter: JsonAdapter<Release> = Moshi.Builder().build().adapter(Release::class.java)

  fun getGithubReleaseInfo(): Deferred<Release?> {
    return GlobalScope.async {
      val request = Request.Builder().url(GITHUB_PROJECT_RELEASES).build()
      val response = client.newCall(request).execute()
      if (response.isSuccessful) {
        adapter.fromJson(response.body()!!.source())
      } else {
        throw IOException("Unexpected code " + response.code().toString())
      }
    }
  }

  companion object {
    const val GITHUB_PROJECT_RELEASES =
      "https://api.github.com/repos/G00fY2/version-compare/releases/latest"
  }
}
