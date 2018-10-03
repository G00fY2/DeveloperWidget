package de.g00fy2.developerwidget.about

import android.R.id
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso3.Picasso
import com.squareup.picasso3.Picasso.Builder
import de.g00fy2.developerwidget.BuildConfig
import de.g00fy2.developerwidget.R.layout
import de.g00fy2.developerwidget.util.CircleTransformation
import de.g00fy2.developerwidget.web.GithubAPI
import de.g00fy2.developerwidget.web.model.Release
import kotlinx.android.synthetic.main.activity_about.author_imageview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class AboutActivity : AppCompatActivity(), CoroutineScope {

  private lateinit var job: Job
  private lateinit var okHttpClient: OkHttpClient
  private lateinit var picasso: Picasso
  private lateinit var githubAPI: GithubAPI

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    job = Job()
    setContentView(layout.activity_about)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.elevation = 0f

    okHttpClient = OkHttpClient.Builder().cache(Cache(this.cacheDir, 10L * 1024 * 1024))
      .addNetworkInterceptor(
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Timber.d(it) }).setLevel(
          HttpLoggingInterceptor.Level.BODY
        )
      ).build()
    picasso = Builder(this).client(okHttpClient).indicatorsEnabled(BuildConfig.DEBUG).build()
    githubAPI = GithubAPI(okHttpClient)

    getGithubReleaseInformation()
  }

  override fun onDestroy() {
    super.onDestroy()
    job.cancel()
  }

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + job

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return if (item?.itemId == id.home) {
      finish()
      true
    } else {
      super.onOptionsItemSelected(item)
    }
  }

  private fun getGithubReleaseInformation() {
    launch {
      val releaseInfo: Release?
      try {
        releaseInfo = async(Dispatchers.IO) {
          githubAPI.getGithubReleaseInfo().await()
        }.await()

        picasso.load(releaseInfo?.author?.avatarUrl).transform(CircleTransformation()).into(author_imageview)
      } catch (e: IOException) {
        Timber.d(e)
      }
    }
  }
}