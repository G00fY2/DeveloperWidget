package com.g00fy2.developerwidget.activities.apkinstall

import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.base.BaseContract.BasePresenter
import com.g00fy2.developerwidget.utils.DIALOG_ACTIVITY_HEIGHT_FACTOR
import com.g00fy2.developerwidget.utils.DIALOG_ACTIVITY_WIDTH_FACTOR
import kotlinx.android.synthetic.main.activity_apk.*
import javax.inject.Inject

class ApkActivity : BaseActivity(R.layout.activity_apk), ApkContract.ApkView {

  @Inject
  lateinit var presenter: ApkContract.ApkPresenter

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
    adapter.setOnApkSelect { selectedCount -> showOptions(selectedCount > 0) }
    adapter.setCommitCallback(Runnable {
      adapter.itemCount.let {
        recyclerview.overScrollMode = if (it == 0) View.OVER_SCROLL_NEVER else View.OVER_SCROLL_ALWAYS
        no_items_textview.visibility = if (it == 0) View.VISIBLE else View.INVISIBLE
        no_items_imageview.visibility = if (it == 0) View.VISIBLE else View.INVISIBLE
      }
    })
    recyclerview.setHasFixedSize(true)
    recyclerview.layoutManager = LinearLayoutManager(this)
    recyclerview.adapter = adapter

    cancel_textview.setOnClickListener { finish() }
    TooltipCompat.setTooltipText(delete_imageview, delete_imageview.contentDescription)
    delete_imageview.setOnClickListener { showConfirmationDialog() }
    TooltipCompat.setTooltipText(clear_imageview, clear_imageview.contentDescription)
    clear_imageview.setOnClickListener {
      adapter.clearSelectedList()
      showOptions(false)
    }
  }

  override fun onResume() {
    super.onResume()
    initView()
  }

  override fun toggleResultView(apkFiles: List<ApkFile>, missingPermissions: Boolean) {
    if (missingPermissions) {
      progressbar.visibility = View.GONE
      no_items_textview.text = getString(R.string.missing_permissions)
    } else {
      no_items_textview.visibility = View.INVISIBLE
      no_items_textview.text = getString(R.string.no_apk_found)
      ViewCompat.animate(progressbar).alpha(0f)
        .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong()).withEndAction {
          progressbar.visibility = View.INVISIBLE
          progressbar.alpha = 1f
        }.start()
    }
    adapter.submitList(apkFiles)
  }

  private fun initView() {
    progressbar.visibility = View.VISIBLE
    no_items_imageview.visibility = View.INVISIBLE
    no_items_textview.text = getString(R.string.scanning_apks)
    no_items_textview.visibility = View.VISIBLE
  }

  private fun showOptions(show: Boolean) {
    delete_header_group.visibility = if (show) View.VISIBLE else View.GONE
  }

  private fun showConfirmationDialog() {
    ApkDeleteDialog(this).init {
      deleteMessage(adapter.getSelectedCount())
      deleteAction {
        showOptions(false)
        progressbar.visibility = View.VISIBLE
        presenter.deleteApkFiles(adapter.getSelectedApkFilesAndClear())
      }
    }.show()
  }
}