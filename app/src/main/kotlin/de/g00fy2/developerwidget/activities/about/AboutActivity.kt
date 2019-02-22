package de.g00fy2.developerwidget.activities.about

import android.R.id
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.ContentView
import androidx.core.net.toUri
import de.g00fy2.developerwidget.BuildConfig
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.base.BaseActivity
import de.g00fy2.developerwidget.utils.*
import kotlinx.android.synthetic.main.activity_about.*
import timber.log.Timber
import javax.inject.Inject

@ContentView(R.layout.activity_about)
class AboutActivity : BaseActivity(), AboutContract.AboutView {

  @Inject lateinit var presenter: AboutContract.AboutPresenter

  private var clickCount = 0
  private var clickStart: Long = 0
  private var toast: Toast? = null

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.elevation = 0f
    initView()
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return if (item?.itemId == id.home) {
      finish()
      true
    } else {
      super.onOptionsItemSelected(item)
    }
  }

  private fun initView() {
    setActionbarElevationListener(about_root_scrollview)

    app_version_textview.text =
      String.format(getString(R.string.app_version), BuildConfig.VERSION_NAME)
    app_desc_textview.text = "App description." // TODO

    privacy_item.init {
      icon(R.drawable.ic_privacy_logo)
      title(R.string.privacy_policy)
      action { openUrl(PRIVACY_POLICY) }
    }
    license_item.init {
      icon(R.drawable.ic_open_source_logo)
      title(R.string.license)
      description(R.string.mit_license)
      action { openUrl(MIT_LICENSE) }
    }
    source_code_item.init {
      icon(R.drawable.ic_github_logo_shape)
      title(R.string.source_code)
      action { openUrl(GITHUB_PROJECT) }
    }
    changelog_item.init {
      icon(R.drawable.ic_changes_logo)
      title(R.string.changelog)
      action { openUrl(CHANGES) }
    }
    author_header.init {
      title(R.string.author)
    }
    twitter_item.init {
      icon(R.drawable.ic_twitter_logo)
      title(R.string.twitter)
      description(R.string.twitter_username)
      action { openUrl(TWITTER_USER) }
    }
    github_item.init {
      icon(R.drawable.ic_github_logo_shape)
      title(R.string.github)
      description(R.string.github_username)
      action { openUrl(GITHUB_USER) }
    }
    licenses_header.init {
      title(R.string.licenses)
    }
    open_source_licenses_item.init {
      title(R.string.open_source_licenses)
      description(R.string.open_source_licenses_description)
      action { openUrl(OSS_LICENSES) }
    }
    image_licenses_item.init {
      title(R.string.icon_credits)
      description(R.string.icon_credits_description)
      action { openUrl(ICON_CREDITS) }
    }
    build_number_item.init {
      title(R.string.build_number)
      description(BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE + "." + BuildConfig.BUILD_TYPE)
      action { honorClicking() }
    }
  }

  private fun openUrl(url: String) {
    if (url.startsWith("http", true)) {
      try {
        val uri = url.toUri()
        startActivity(Intent(Intent.ACTION_VIEW, uri))
      } catch (e: Exception) {
        Timber.d(e)
      }
    }
  }

  private fun honorClicking() {
    val current = System.currentTimeMillis()
    if (current - clickStart > 3000) clickCount = 0
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
}