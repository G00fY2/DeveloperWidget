package de.g00fy2.developerwidget.web

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import de.g00fy2.developerwidget.util.Constants
import de.g00fy2.developerwidget.web.model.Release
import de.g00fy2.developerwidget.web.model.Repository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class GithubAPI(private var client: OkHttpClient) {

  private val repositoryAdapter: JsonAdapter<Repository> = Moshi.Builder().build().adapter(Repository::class.java)
  private val releaseAdapter: JsonAdapter<Release> = Moshi.Builder().build().adapter(Release::class.java)

  fun getGithubRepositoryInfo(): Deferred<Repository?> {
    return GlobalScope.async {
      val request = Request.Builder().url(Constants.GITHUB_API_PROJECT).build()
      val response = client.newCall(request).execute()
      if (response.isSuccessful) {
        repositoryAdapter.fromJson(response.body()!!.source())
      } else {
        throw IOException("Unexpected code " + response.code().toString())
      }
    }
  }

  fun getGithubReleaseInfo(): Deferred<Release?> {
    return GlobalScope.async {
      val request = Request.Builder().url(Constants.GITHUB_API_PROJECT_RELEASE).build()
      val response = client.newCall(request).execute()
      if (response.isSuccessful) {
        releaseAdapter.fromJson(response.body()!!.source())
      } else {
        throw IOException("Unexpected code " + response.code().toString())
      }
    }
  }
}
