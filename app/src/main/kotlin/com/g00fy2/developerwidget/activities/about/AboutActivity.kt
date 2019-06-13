package com.g00fy2.developerwidget.activities.about

import android.R.id
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import com.g00fy2.developerwidget.BuildConfig
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.activities.widgetconfig.ConfigLauncherActivity
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.base.BaseContract.BasePresenter
import com.g00fy2.developerwidget.utils.CHANGES
import com.g00fy2.developerwidget.utils.GITHUB_ISSUE
import com.g00fy2.developerwidget.utils.GITHUB_PROJECT
import com.g00fy2.developerwidget.utils.GITHUB_USER
import com.g00fy2.developerwidget.utils.ICON_CREDITS
import com.g00fy2.developerwidget.utils.MIT_LICENSE
import com.g00fy2.developerwidget.utils.OSS_LICENSES
import com.g00fy2.developerwidget.utils.PRIVACY_POLICY
import com.g00fy2.developerwidget.utils.TWITTER_USER
import kotlinx.android.synthetic.main.activity_about.*
import javax.inject.Inject

class AboutActivity : BaseActivity(R.layout.activity_about), AboutContract.AboutView {

  @Inject
  lateinit var presenter: AboutContract.AboutPresenter

  override fun providePresenter(): BasePresenter = presenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initView()
  }

  override fun onResume() {
    super.onResume()
    updateThemeToggleView()
    updateLauncherIconSwitch()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return if (item.itemId == id.home) {
      finish()
      true
    } else {
      super.onOptionsItemSelected(item)
    }
  }

  private fun initView() {
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    setActionbarElevationListener(about_root_scrollview)

    app_version_textview.text = String.format(getString(R.string.app_version), BuildConfig.VERSION_NAME)

    theme_item.init {
      title(R.string.app_theme)
      action { presenter.toggleDayNightMode() }
    }
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
    feedback_item.init {
      icon(R.drawable.ic_feedback)
      title(R.string.feedback)
      description(R.string.feedback_description)
      action { showFeedbackOptions() }
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
    hide_launcher_icon_item.init {
      title(R.string.show_app_icon)
      description(R.string.show_app_icon_description)
      action { toggleLauncherIcon() }
    }
    build_number_item.init {
      title(R.string.build_number)
      description(BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ") " + BuildConfig.BUILD_TYPE)
      action { presenter.honorClicking() }
    }
  }

  override fun updateThemeToggleView() {
    theme_item.let {
      when (dayNightController.getCurrentDefaultMode()) {
        AppCompatDelegate.MODE_NIGHT_YES -> it.icon(R.drawable.ic_mode_night).description(R.string.night_mode)
        AppCompatDelegate.MODE_NIGHT_NO -> it.icon(R.drawable.ic_mode_day).description(R.string.day_mode)
        else -> it.icon(R.drawable.ic_mode_auto).description(R.string.auto_mode)
      }
    }
  }

  private fun updateLauncherIconSwitch() = hide_launcher_icon_item.switch(!isLauncherIconDisabled())

  private fun showFeedbackOptions() {
    AboutFeedbackDialog(this).init {
      mailAction { presenter.sendFeedbackMail() }
      githubAction { presenter.openUrl(GITHUB_ISSUE) }
    }.show()
  }

  private fun isLauncherIconDisabled(): Boolean {
    return packageManager.getComponentEnabledSetting(
      ComponentName(
        this,
        ConfigLauncherActivity::class.java
      )
    ) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
  }

  private fun toggleLauncherIcon() {
    if (isLauncherIconDisabled()) {
      PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    } else {
      PackageManager.COMPONENT_ENABLED_STATE_DISABLED
    }.let {
      packageManager.setComponentEnabledSetting(
        ComponentName(this, ConfigLauncherActivity::class.java),
        it,
        PackageManager.DONT_KILL_APP
      )
    }
    presenter.showRebootNotice()
  }
}