package com.g00fy2.developerwidget.activities.shortcut

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.g00fy2.developerwidget.R
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
    getDrawable(R.drawable.divider_line)?.let {
      recyclerview.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL).apply { setDrawable(it) })
    }

    adapter.submitList(shortcutInfoList)
    adapter.setOnShortcutSelected { shortcutPosition -> onItemClick(shortcutPosition) }
  }

  @Suppress("DEPRECATION")
  fun onItemClick(position: Int) {
    var shortcutIntent: Intent? = null

    shortcutInfoList?.get(position)?.let { shortcutInfo ->
      shortcutIntent = if (VERSION.SDK_INT >= VERSION_CODES.O) {
        shortcutManager?.createShortcutResultIntent(shortcutInfo)
      } else {
        Intent().apply {
          putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutInfo.intent)
          putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutInfo.shortLabel)
          putExtra(Intent.EXTRA_SHORTCUT_ICON, getBadgedIconCompat(shortcutInfo))
        }
      }
    }

    setResult(if (shortcutIntent == null) Activity.RESULT_CANCELED else Activity.RESULT_OK, shortcutIntent)
    finish()
  }

  private fun getBadgedIconCompat(shortcutInfo: ShortcutInfo): Bitmap {
    val appIcon = applicationInfo.loadIcon(packageManager)
    val defaultSize = (DEFAULT_ICON_SIZE_DP * resources.displayMetrics.density).toInt()

    (Class.forName("android.content.pm.ShortcutInfo").getMethod("getIconResourceId").invoke(shortcutInfo) as Int?)?.let { iconRes ->
      AppCompatResources.getDrawable(this, iconRes)?.toBitmap(defaultSize, defaultSize)?.let { iconBitmap ->
        val w = iconBitmap.width
        val h = iconBitmap.height
        appIcon.setBounds(w / 2, h / 2, w, h)
        appIcon.draw(Canvas(iconBitmap))

        return iconBitmap
      }
    }

    return appIcon.toBitmap(defaultSize, defaultSize)
  }

  companion object {
    private const val DEFAULT_ICON_SIZE_DP = 60f
  }
}