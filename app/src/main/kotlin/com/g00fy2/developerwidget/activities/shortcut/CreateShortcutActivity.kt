package com.g00fy2.developerwidget.activities.shortcut

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.activities.widgetconfig.WidgetConfigActivity
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.base.BaseContract.BasePresenter
import kotlinx.android.synthetic.main.activity_create_shortcut.*
import javax.inject.Inject


@TargetApi(VERSION_CODES.N_MR1)
class CreateShortcutActivity : BaseActivity(R.layout.activity_create_shortcut),
  CreateShortcutContract.CreateShortcutView {

  @Inject
  lateinit var presenter: CreateShortcutContract.CreateShortcutPresenter
  private lateinit var adapter: ShortcutAdapter
  private val shortcutManager by lazy { getSystemService<ShortcutManager>() }
  private val shortcutInfoList by lazy { shortcutManager?.manifestShortcuts }

  override fun providePresenter(): BasePresenter = presenter

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setResult(Activity.RESULT_CANCELED)

    adapter = ShortcutAdapter()
    recyclerview.setHasFixedSize(true)
    recyclerview.layoutManager = LinearLayoutManager(this)
    recyclerview.adapter = adapter

    adapter.submitList(shortcutInfoList)
    adapter.setOnShortcutSelected { shortcutPosition -> onItemClick(shortcutPosition) }
  }

  @Suppress("DEPRECATION")
  fun onItemClick(position: Int) {
    var shortcutIntent: Intent? = null

    shortcutInfoList?.get(position)?.let { shortcutInfo ->
      if (VERSION.SDK_INT >= VERSION_CODES.O) {
        shortcutIntent = shortcutManager?.createShortcutResultIntent(shortcutInfo)
      } else {

        // TODO implement pre oreo shortcuts
        val icon = Intent.ShortcutIconResource.fromContext(this, getShortcutIcon(shortcutInfo))

        shortcutIntent = Intent().apply {
          putExtra(Intent.EXTRA_SHORTCUT_INTENT, getOpenShortcutActivityIntent(shortcutInfo))
          putExtra(Intent.EXTRA_SHORTCUT_NAME, getShortcutName(shortcutInfo))
          putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon)
        }
      }
    }

    setResult(if (shortcutIntent == null) Activity.RESULT_CANCELED else Activity.RESULT_OK, shortcutIntent)
    finish()
  }

  private fun getShortcutName(shortcutInfo: ShortcutInfo): CharSequence {
    return shortcutInfo.shortLabel ?: resources.getString(R.string.app_name)
  }

  private fun getOpenShortcutActivityIntent(shortcutInfo: ShortcutInfo): Intent {
    return Intent(this, WidgetConfigActivity::class.java)
  }

  @DrawableRes
  private fun getShortcutIcon(shortcutInfo: ShortcutInfo): Int {
    return R.mipmap.ic_launcher
  }
}
