package de.g00fy2.developerwidget.about

import android.R.id
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import de.g00fy2.developerwidget.BuildConfig
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.R.layout
import de.g00fy2.developerwidget.util.Constants
import de.g00fy2.developerwidget.util.SharedPreferencesHelper
import de.g00fy2.developerwidget.util.ViewUtils
import kotlinx.android.synthetic.main.activity_about.about_root_scrollview
import kotlinx.android.synthetic.main.activity_about.app_desc_textview
import kotlinx.android.synthetic.main.activity_about.app_version_textview
import kotlinx.android.synthetic.main.activity_about.author_header
import kotlinx.android.synthetic.main.activity_about.build_number_item
import kotlinx.android.synthetic.main.activity_about.changelog_item
import kotlinx.android.synthetic.main.activity_about.github_item
import kotlinx.android.synthetic.main.activity_about.image_licenses_item
import kotlinx.android.synthetic.main.activity_about.license_item
import kotlinx.android.synthetic.main.activity_about.licenses_header
import kotlinx.android.synthetic.main.activity_about.open_source_licenses_item
import kotlinx.android.synthetic.main.activity_about.privacy_item
import kotlinx.android.synthetic.main.activity_about.source_code_item
import kotlinx.android.synthetic.main.activity_about.twitter_item
import timber.log.Timber

class AboutActivity : AppCompatActivity() {

  private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_about)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.elevation = 0f
    sharedPreferencesHelper = SharedPreferencesHelper(this)
    initView()
  }

  private fun initView() {
    about_root_scrollview.viewTreeObserver.addOnScrollChangedListener {
      changeActionbarElevation()
    }
    app_version_textview.text =
        String.format(getString(R.string.app_version), BuildConfig.VERSION_NAME)
    app_desc_textview.text = "App description." // TODO

    privacy_item.setIcon(R.drawable.ic_privacy_logo).setTitle(R.string.privacy_policy)
      .setAction { openUrl(Constants.PRIVACY_POLICY) }
    license_item.setIcon(R.drawable.ic_open_source_logo).setTitle(R.string.license).setDescription(R.string.mit_license)
    source_code_item.setIcon(R.drawable.ic_github_logo_shape).setTitle(R.string.source_code)
      .setAction { openUrl(Constants.GITHUB_PROJECT) }
    changelog_item.setIcon(R.drawable.ic_changes_logo).setTitle(R.string.changelog)

    author_header.setTitle(R.string.author)
    twitter_item.setIcon(R.drawable.ic_twitter_logo).setTitle(R.string.twitter)
      .setAction { openUrl(Constants.TWITTER_USER) }
      .setDescription(R.string.twitter_username)
    github_item.setIcon(R.drawable.ic_github_logo_shape).setTitle(R.string.github)
      .setDescription(R.string.github_username)
      .setAction { openUrl(Constants.GITHUB_USER) }

    licenses_header.setTitle(R.string.licenses)
    open_source_licenses_item.setTitle(R.string.open_source_licenses)
    image_licenses_item.setTitle(R.string.icon_credits).setAction { openUrl(Constants.ICON_CREDITS) }
    build_number_item.setTitle(R.string.build_number)
      .setDescription(BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE + "." + BuildConfig.BUILD_TYPE)
      .enableHonorClicking(true)
  }

  private fun changeActionbarElevation() {
    val scrollViewY = about_root_scrollview.scrollY.toFloat()
    if (scrollViewY <= 0f) {
      supportActionBar?.elevation = 0f
    } else {
      var elevationDp = scrollViewY / 4 // divide scrollY to increase fade in range
      if (elevationDp > 4f) {
        elevationDp = 4f
      }
      supportActionBar?.elevation = ViewUtils.dpToPx(elevationDp, this)
    }
  }

  private fun openUrl(url: String) {
    if (url.startsWith("http", true)) {
      try {
        val uri = Uri.parse(url)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
      } catch (e: Exception) {
        Timber.d(e)
      }
    }
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return if (item?.itemId == id.home) {
      finish()
      true
    } else {
      super.onOptionsItemSelected(item)
    }
  }
}