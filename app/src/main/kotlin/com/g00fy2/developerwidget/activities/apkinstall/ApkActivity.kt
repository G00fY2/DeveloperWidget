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

    adapter = ApkAdapter().setOnApkSelected { install_textview.isEnabled = true }
    recyclerview.setHasFixedSize(true)
    recyclerview.layoutManager = LinearLayoutManager(this)
    recyclerview.adapter = adapter

    cancel_textview.setOnClickListener { finish() }
    install_textview.setOnClickListener { presenter.installApk(adapter.getSelectedFile()?.fileUri) }
  }

  override fun onResume() {
    super.onResume()
    initState()
  }

  override fun toggleResultView(apkFiles: List<ApkFile>, missingPermissions: Boolean) {
    if (missingPermissions) {
      progressbar.visibility = View.INVISIBLE
      progress_textview.text = getString(R.string.missing_permissions)
      return
    }

    if (apkFiles.isNotEmpty()) {
      adapter.submitList(apkFiles)
      recyclerview.overScrollMode = View.OVER_SCROLL_ALWAYS
      progress_textview.visibility = View.INVISIBLE
    } else {
      progress_textview.text = getString(R.string.no_apk_found)
    }

    ViewCompat.animate(progressbar).alpha(0f)
      .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong()).withEndAction {
        progressbar.visibility = View.INVISIBLE
      }.start()
  }

  private fun initState() {
    install_textview.isEnabled = false
    progressbar.alpha = 1f
    progressbar.visibility = View.VISIBLE
    progress_textview.visibility = View.VISIBLE
    progress_textview.text = getString(R.string.scanning_apks)
    recyclerview.overScrollMode = View.OVER_SCROLL_NEVER
    adapter.clearList()
  }
}