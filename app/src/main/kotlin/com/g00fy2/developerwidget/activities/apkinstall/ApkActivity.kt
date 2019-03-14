package com.g00fy2.developerwidget.activities.apkinstall

import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.annotation.ContentView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.base.BaseContract.BasePresenter
import com.g00fy2.developerwidget.utils.DIALOG_ACTIVITY_HEIGHT_FACTOR
import com.g00fy2.developerwidget.utils.DIALOG_ACTIVITY_WIDTH_FACTOR
import kotlinx.android.synthetic.main.activity_apk.*
import javax.inject.Inject

@ContentView(R.layout.activity_apk)
class ApkActivity : BaseActivity(), ApkContract.ApkView {

  @Inject lateinit var presenter: ApkContract.ApkPresenter

  private lateinit var adapter: ApkAdapter

  override fun providePresenter(): BasePresenter = presenter

  override fun onCreate(savedInstanceState: Bundle?) {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    super.onCreate(savedInstanceState)

    val width = (resources.displayMetrics.widthPixels * DIALOG_ACTIVITY_WIDTH_FACTOR).toInt()
    val height = (resources.displayMetrics.heightPixels * DIALOG_ACTIVITY_HEIGHT_FACTOR).toInt()
    window.setLayout(width, height)

    adapter = ApkAdapter()
    adapter.setOnApkClicked { apkFile -> presenter.installApk(apkFile) }
    adapter.setOnApkLongClicked { selectedCount -> showOptions(selectedCount) }
    recyclerview.setHasFixedSize(true)
    recyclerview.layoutManager = LinearLayoutManager(this)
    recyclerview.adapter = adapter

    cancel_textview.setOnClickListener { finish() }
  }

  private fun showOptions(selectedCount: Int) {
    // TODO
  }

  override fun onResume() {
    super.onResume()
    initState()
  }

  override fun toggleResultView(apkFiles: List<ApkFile>, missingPermissions: Boolean) {
    if (missingPermissions) {
      progressbar.visibility = View.INVISIBLE
      no_items_textview.text = getString(R.string.missing_permissions)
      no_items_imageview.visibility = View.VISIBLE
      return
    }

    if (apkFiles.isNotEmpty()) {
      adapter.submitList(apkFiles)
      recyclerview.overScrollMode = View.OVER_SCROLL_ALWAYS
      no_items_textview.visibility = View.INVISIBLE
      no_items_imageview.visibility = View.INVISIBLE
    } else {
      no_items_textview.text = getString(R.string.no_apk_found)
      no_items_imageview.visibility = View.VISIBLE
    }

    ViewCompat.animate(progressbar).alpha(0f)
      .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong()).withEndAction {
        progressbar.visibility = View.INVISIBLE
      }.start()
  }

  private fun initState() {
    progressbar.alpha = 1f
    progressbar.visibility = View.VISIBLE
    no_items_textview.visibility = View.VISIBLE
    no_items_textview.text = getString(R.string.scanning_apks)
    no_items_imageview.visibility = View.INVISIBLE
    recyclerview.overScrollMode = View.OVER_SCROLL_NEVER
    adapter.clearList()
  }
}