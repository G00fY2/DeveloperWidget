package de.g00fy2.developerwidget.about

import android.R.id
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso3.Picasso
import com.squareup.picasso3.Picasso.Builder
import de.g00fy2.developerwidget.BuildConfig
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.R.layout
import de.g00fy2.developerwidget.util.Constants
import de.g00fy2.developerwidget.util.SharedPreferencesHelper
import de.g00fy2.developerwidget.web.GithubAPI
import de.g00fy2.developerwidget.web.model.Release
import de.g00fy2.developerwidget.web.model.Repository
import kotlinx.android.synthetic.main.activity_about.app_desc_textview
import kotlinx.android.synthetic.main.activity_about.app_version_textview
import kotlinx.android.synthetic.main.activity_about.author_header
import kotlinx.android.synthetic.main.activity_about.build_number_item
import kotlinx.android.synthetic.main.activity_about.changelog_item
import kotlinx.android.synthetic.main.activity_about.github_item
import kotlinx.android.synthetic.main.activity_about.license_item
import kotlinx.android.synthetic.main.activity_about.licenses_header
import kotlinx.android.synthetic.main.activity_about.open_source_licenses_item
import kotlinx.android.synthetic.main.activity_about.privacy_item
import kotlinx.android.synthetic.main.activity_about.source_code_item
import kotlinx.android.synthetic.main.activity_about.twitter_item
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
  private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    job = Job()
    setContentView(layout.activity_about)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.elevation = 0f
    setupDependencies()
    initView()
    getGithubInformation()
  }

  private fun initView() {
    app_version_textview.text =
        String.format(getString(R.string.app_version), BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
    app_desc_textview.text = sharedPreferencesHelper.getString(SharedPreferencesHelper.GITHUB_PROJECT_DESC) ?: ""

    privacy_item.setIcon(R.drawable.ic_privacy_logo).setTitle(R.string.privacy_policy)
    license_item.setIcon(R.drawable.ic_open_source_logo).setTitle(R.string.license).setDescription(R.string.mit_license)
    source_code_item.setIcon(R.drawable.ic_github_logo_shape).setTitle(R.string.source_code)
    changelog_item.setIcon(R.drawable.ic_changes_logo).setTitle(R.string.changelog)

    author_header.setTitle(R.string.author)
    twitter_item.setIcon(R.drawable.ic_github_logo_shape).setTitle(R.string.twitter).setUrl(Constants.TWITTER_USER)
      .setDescription(R.string.twitter_username)
    github_item.setIcon(R.drawable.ic_twitter_logo).setTitle(R.string.github).setDescription(R.string.github_username)
      .setUrl(Constants.GITHUB_USER)

    licenses_header.setTitle(R.string.licenses)
    open_source_licenses_item.setTitle(R.string.open_source_licenses)
    build_number_item.setIcon(R.drawable.ic_about_logo).setTitle(R.string.build_number)
      .setDescription(BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE + "." + BuildConfig.BUILD_TYPE)
      .enableHonorClicking(true)
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

  private fun setupDependencies() {
    sharedPreferencesHelper = SharedPreferencesHelper(this)
    okHttpClient = OkHttpClient.Builder().cache(Cache(this.cacheDir, 10L * 1024 * 1024))
      .addNetworkInterceptor(
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Timber.d(it) }).setLevel(
          HttpLoggingInterceptor.Level.BODY
        )
      ).build()
    picasso = Builder(this).client(okHttpClient).indicatorsEnabled(BuildConfig.DEBUG).build()
    githubAPI = GithubAPI(okHttpClient)
  }

  private fun getGithubInformation() {
    launch {
      val releaseInfo: Release?
      val repositoryInfo: Repository?
      try {
        releaseInfo = async(Dispatchers.IO) {
          githubAPI.getGithubReleaseInfo().await()
        }.await()
        repositoryInfo = async(Dispatchers.IO) {
          githubAPI.getGithubRepositoryInfo().await()
        }.await()

        sharedPreferencesHelper.putString(SharedPreferencesHelper.GITHUB_PROJECT_DESC, repositoryInfo?.description)
        sharedPreferencesHelper.putString(
          SharedPreferencesHelper.GITHUB_AUTHOR_IMAGE_URL,
          releaseInfo?.author?.avatarUrl
        )
        app_desc_textview.text = repositoryInfo?.description ?: ""
      } catch (e: IOException) {
        Timber.d(e)
      }
    }
  }
}