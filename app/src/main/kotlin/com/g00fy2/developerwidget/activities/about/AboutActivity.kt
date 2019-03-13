package com.g00fy2.developerwidget.activities.about

import android.R.id
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.ContentView
import com.g00fy2.developerwidget.BuildConfig
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.base.BaseContract.BasePresenter
import com.g00fy2.developerwidget.utils.*
import kotlinx.android.synthetic.main.activity_about.*
import javax.inject.Inject

@ContentView(R.layout.activity_about)
class AboutActivity : BaseActivity(), AboutContract.AboutView {

  @Inject lateinit var presenter: AboutContract.AboutPresenter

  override fun providePresenter(): BasePresenter = presenter

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
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
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    setActionbarElevationListener(about_root_scrollview)

    app_version_textview.text =
      String.format(getString(R.string.app_version), BuildConfig.VERSION_NAME)
    app_desc_textview.text = getString(R.string.app_description)

    privacy_item.init {
      icon(R.drawable.ic_privacy_logo)
      title(R.string.privacy_policy)
      action { presenter.openUrl(PRIVACY_POLICY) }
    }
    license_item.init {
      icon(R.drawable.ic_open_source_logo)
      title(R.string.license)
      description(R.string.mit_license)
      action { presenter.openUrl(MIT_LICENSE) }
    }
    source_code_item.init {
      icon(R.drawable.ic_github_logo_shape)
      title(R.string.source_code)
      action { presenter.openUrl(GITHUB_PROJECT) }
    }
    changelog_item.init {
      icon(R.drawable.ic_changes_logo)
      title(R.string.changelog)
      action { presenter.openUrl(CHANGES) }
    }
    author_header.init {
      title(R.string.author)
    }
    twitter_item.init {
      icon(R.drawable.ic_twitter_logo)
      title(R.string.twitter)
      description(R.string.twitter_username)
      action { presenter.openUrl(TWITTER_USER) }
    }
    github_item.init {
      icon(R.drawable.ic_github_logo_shape)
      title(R.string.github)
      description(R.string.github_username)
      action { presenter.openUrl(GITHUB_USER) }
    }
    licenses_header.init {
      title(R.string.licenses)
    }
    open_source_licenses_item.init {
      title(R.string.open_source_licenses)
      description(R.string.open_source_licenses_description)
      action { presenter.openUrl(OSS_LICENSES) }
    }
    image_licenses_item.init {
      title(R.string.icon_credits)
      description(R.string.icon_credits_description)
      action { presenter.openUrl(ICON_CREDITS) }
    }
    build_number_item.init {
      title(R.string.build_number)
      description(BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE + "." + BuildConfig.BUILD_TYPE)
      action { presenter.honorClicking() }
    }
  }
}