package de.g00fy2.developerwidget.about

import android.R.id
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
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

  private var clickCount = 0
  private var clickStart: Long = 0
  private var toast: Toast? = null
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

    privacy_item.init {
      icon(R.drawable.ic_privacy_logo)
      title(R.string.privacy_policy)
      action { openUrl(Constants.PRIVACY_POLICY) }
    }
    license_item.init {
      icon(R.drawable.ic_open_source_logo)
      title(R.string.license)
      description(R.string.mit_license)
      action { openUrl(Constants.MIT_LICENSE) }
    }
    source_code_item.init {
      icon(R.drawable.ic_github_logo_shape)
      title(R.string.source_code)
      action { openUrl(Constants.GITHUB_PROJECT) }
    }
    changelog_item.init {
      icon(R.drawable.ic_changes_logo)
      title(R.string.changelog)
      action { openUrl(Constants.CHANGES) }
    }
    author_header.init {
      title(R.string.author)
    }
    twitter_item.init {
      icon(R.drawable.ic_twitter_logo)
      title(R.string.twitter)
      description(R.string.twitter_username)
      action { openUrl(Constants.TWITTER_USER) }
    }
    github_item.init {
      icon(R.drawable.ic_github_logo_shape)
      title(R.string.github)
      description(R.string.github_username)
      action { openUrl(Constants.GITHUB_USER) }
    }
    licenses_header.init {
      title(R.string.licenses)
    }
    open_source_licenses_item.init {
      title(R.string.open_source_licenses)
      action { openUrl(Constants.OSS_LICENSES) }
    }
    image_licenses_item.init {
      title(R.string.icon_credits)
      action { openUrl(Constants.ICON_CREDITS) }
    }
    build_number_item.init {
      title(R.string.build_number)
      description(BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE + "." + BuildConfig.BUILD_TYPE)
      action { honorClicking() }
    }
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

  private fun honorClicking() {
    val current = System.currentTimeMillis()
    if (current - clickStart > 3000) {
      clickCount = 0
    }
    clickCount++
    if (clickCount <= 7) {
      clickStart = current
      toast?.cancel()
    }
    if (clickCount in 3..6) {
      val missingSteps = (7 - clickCount)
      toast = Toast.makeText(
        this,
        "You are now " + missingSteps + (if (missingSteps > 1) " steps" else " step") + " away from being a developer.",
        Toast.LENGTH_SHORT
      )
      toast?.show()
    } else if (clickCount == 7) {
      toast = Toast.makeText(this, "You are a real developer!", Toast.LENGTH_SHORT)
      toast?.show()
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