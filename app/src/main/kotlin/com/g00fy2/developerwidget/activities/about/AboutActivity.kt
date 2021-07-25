package com.g00fy2.developerwidget.activities.about

import android.R.id
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.g00fy2.developerwidget.BuildConfig
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.activities.about.dialogs.AboutFeedbackDialog
import com.g00fy2.developerwidget.activities.about.dialogs.SearchDepthDialog
import com.g00fy2.developerwidget.activities.widgetconfig.ConfigLauncherActivity
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.base.BaseContract.BasePresenter
import com.g00fy2.developerwidget.databinding.ActivityAboutBinding
import com.g00fy2.developerwidget.ktx.doOnApplyWindowInsets
import javax.inject.Inject

class AboutActivity : BaseActivity(), AboutContract.AboutView {

  @Inject
  lateinit var presenter: AboutContract.AboutPresenter

  override val binding: ActivityAboutBinding by viewBinding(ActivityAboutBinding::inflate)
  override fun providePresenter(): BasePresenter = presenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (intent.getBooleanExtra(SCROLL_BOTTOM, false)) {
      intent.removeExtra(SCROLL_BOTTOM)
      binding.aboutRootScrollview.run {
        postDelayed({ smoothScrollTo(0, bottom) }, 300)
        setPressedState(binding.searchDepthItem, true, 600)
        setPressedState(binding.searchDepthItem, false, 800)
        setPressedState(binding.searchDepthItem, true, 1000)
      }
    }
  }

  override fun initView() {
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    setActionbarElevationListener(binding.aboutRootScrollview)

    binding.appVersionTextview.text = String.format(getString(R.string.app_version), BuildConfig.VERSION_NAME)

    binding.themeItem.init {
      title(R.string.app_theme)
      action { presenter.toggleDayNightMode() }
    }
    binding.privacyItem.init {
      icon(R.drawable.ic_privacy_logo)
      title(R.string.privacy_policy)
      action { presenter.openUrl(PRIVACY_POLICY) }
    }
    binding.sourceCodeItem.init {
      icon(R.drawable.ic_github_logo_shape)
      title(R.string.source_code)
      action { presenter.openUrl(GITHUB_PROJECT) }
    }
    binding.changelogItem.init {
      icon(R.drawable.ic_changes_logo)
      title(R.string.changelog)
      action { presenter.openUrl(CHANGES) }
    }
    binding.licenseItem.init {
      icon(R.drawable.ic_open_source_logo)
      title(R.string.license)
      description(R.string.mit_license)
      action { presenter.openUrl(MIT_LICENSE) }
    }
    binding.feedbackItem.init {
      icon(R.drawable.ic_feedback)
      title(R.string.feedback)
      description(R.string.feedback_description)
      action { showFeedbackOptions() }
    }
    binding.authorHeader.init {
      title(R.string.author)
    }
    binding.twitterItem.init {
      icon(R.drawable.ic_twitter_logo)
      title(R.string.twitter)
      description(R.string.twitter_username)
      action { presenter.openUrl(TWITTER_USER) }
    }
    binding.githubItem.init {
      icon(R.drawable.ic_github_logo_shape)
      title(R.string.github)
      description(R.string.github_username)
      action { presenter.openUrl(GITHUB_USER) }
    }
    binding.licensesHeader.init {
      title(R.string.licenses)
    }
    binding.openSourceLicensesItem.init {
      title(R.string.open_source_licenses)
      description(R.string.open_source_licenses_description)
      action { presenter.openUrl(OSS_LICENSES) }
    }
    binding.imageLicensesItem.init {
      title(R.string.icon_credits)
      description(R.string.icon_credits_description)
      action { presenter.openUrl(ICON_CREDITS) }
    }
    binding.searchDepthItem.init {
      title(R.string.search_apk_depth)
    }
    binding.hideLauncherIconItem.init {
      title(R.string.show_app_icon)
      description(R.string.show_app_icon_description)
      action { toggleLauncherIcon() }
    }
    binding.buildNumberItem.init {
      title(R.string.build_number)
      description(BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ") " + BuildConfig.BUILD_TYPE)
      action { presenter.honorClicking() }
    }
    if (VERSION.SDK_INT >= VERSION_CODES.O_MR1) {
      binding.aboutRootScrollview.apply {
        doOnApplyWindowInsets { _, insets, padding, _ ->
          updatePadding(bottom = padding.bottom + insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom)
        }
        viewTreeObserver.addOnScrollChangedListener {
          val scrollableRange = getChildAt(0).bottom - height + paddingBottom
          if (!isGesturalNavMode()) {
            clipToPadding = (scrollY >= scrollableRange)
          }
        }
      }
    }
  }

  override fun onResume() {
    super.onResume()
    updateThemeToggleView()
    updateLauncherIconSwitch()
    updateLauncherIconItem()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return if (item.itemId == id.home) {
      finish()
      true
    } else {
      super.onOptionsItemSelected(item)
    }
  }

  override fun updateThemeToggleView() {
    binding.themeItem.let {
      when (dayNightController.getCurrentDefaultMode()) {
        AppCompatDelegate.MODE_NIGHT_YES -> it.icon(R.drawable.ic_mode_night).description(R.string.night_mode)
        AppCompatDelegate.MODE_NIGHT_NO -> it.icon(R.drawable.ic_mode_day).description(R.string.day_mode)
        else -> it.icon(R.drawable.ic_mode_auto).description(R.string.auto_mode)
      }
    }
  }

  override fun updateSearchDepthUi(depth: Int) {
    val description = when (depth) {
      0 -> getString(R.string.infinite_depth)
      2 -> getString(R.string.default_depth, depth)
      else -> depth.toString()
    }
    binding.searchDepthItem.description(description)
    binding.searchDepthItem.action { showSearchDepthOptions(depth) }
  }

  private fun updateLauncherIconSwitch() = binding.hideLauncherIconItem.switch(!isLauncherIconDisabled())

  private fun updateLauncherIconItem() {
    if (VERSION.SDK_INT >= VERSION_CODES.Q) binding.hideLauncherIconItem.isEnabled = isLauncherIconDisabled()
  }

  private fun showFeedbackOptions() {
    AboutFeedbackDialog(this).init {
      mailAction { presenter.sendFeedbackMail() }
      githubAction { presenter.openUrl(GITHUB_ISSUE) }
    }.show()
  }

  private fun showSearchDepthOptions(currentDepth: Int) {
    SearchDepthDialog(this).init {
      initialValue(currentDepth)
      onPositive { presenter.saveSearchDepth(it) }
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
    updateLauncherIconItem()
    presenter.showRebootNotice()
  }

  private fun setPressedState(item: View, pressed: Boolean, delay: Int) {
    item.run { postDelayed({ isPressed = pressed }, delay.toLong()) }
  }

  companion object {
    const val SCROLL_BOTTOM = "scrollBottom"
  }
}