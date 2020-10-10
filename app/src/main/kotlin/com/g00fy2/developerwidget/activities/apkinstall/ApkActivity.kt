package com.g00fy2.developerwidget.activities.apkinstall

import android.content.Intent
import android.view.View
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.activities.about.AboutActivity
import com.g00fy2.developerwidget.activities.about.AboutActivity.Companion.SCROLL_BOTTOM
import com.g00fy2.developerwidget.activities.apkinstall.dialogs.ApkDeleteDialog
import com.g00fy2.developerwidget.activities.apkinstall.dialogs.ApkWarningDialog
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.base.BaseContract.BasePresenter
import com.g00fy2.developerwidget.databinding.ActivityApkBinding
import com.g00fy2.developerwidget.ktx.setClickableText
import javax.inject.Inject

class ApkActivity : BaseActivity(true), ApkContract.ApkView {

  @Inject
  lateinit var presenter: ApkContract.ApkPresenter

  override val binding: ActivityApkBinding by viewBinding(ActivityApkBinding::inflate)
  private lateinit var adapter: ApkAdapter

  override fun providePresenter(): BasePresenter = presenter

  override fun initView() {
    adapter = ApkAdapter()
    adapter.setOnApkClicked { apkFile -> presenter.installOrShowPermissionWarning(apkFile) }
    adapter.setOnApkSelect { selectedCount -> showOptions(selectedCount > 0) }
    adapter.setCommitCallback {
      adapter.itemCount.let {
        binding.recyclerview.overScrollMode = if (it == 0) View.OVER_SCROLL_NEVER else View.OVER_SCROLL_ALWAYS
        binding.noItemsTextview.visibility = if (it == 0) View.VISIBLE else View.INVISIBLE
        binding.noItemsImageview.visibility = if (it == 0) View.VISIBLE else View.INVISIBLE
      }
    }
    binding.recyclerview.setHasFixedSize(true)
    binding.recyclerview.layoutManager = LinearLayoutManager(this)
    binding.recyclerview.adapter = adapter

    binding.cancelTextview.setOnClickListener { finish() }
    TooltipCompat.setTooltipText(binding.deleteImageview, binding.deleteImageview.contentDescription)
    binding.deleteImageview.setOnClickListener { showConfirmationDialog() }
    TooltipCompat.setTooltipText(binding.clearImageview, binding.clearImageview.contentDescription)
    binding.clearImageview.setOnClickListener {
      adapter.clearSelectedList()
      showOptions(false)
    }
  }

  override fun toggleResultView(apkFiles: List<ApkFile>, missingPermissions: Boolean, searchDepth: Int) {
    if (missingPermissions) {
      binding.progressbar.visibility = View.GONE
      binding.noItemsTextview.text = getString(R.string.missing_permissions)
    } else {
      binding.noItemsTextview.visibility = View.INVISIBLE
      if (searchDepth != Int.MAX_VALUE) {
        binding.noItemsTextview.setClickableText(
          getString(R.string.no_apk_found) + "\n" + getString(R.string.no_apk_found_search_depth, searchDepth),
          getString(R.string.no_apk_found_search_depth, searchDepth)
        ) { openAboutActivity() }
      } else {
        binding.noItemsTextview.text = getString(R.string.no_apk_found)
      }

      ViewCompat.animate(binding.progressbar).alpha(0f)
        .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong()).withEndAction {
          binding.progressbar.visibility = View.INVISIBLE
          binding.progressbar.alpha = 1f
        }.start()
    }
    adapter.submitList(apkFiles)
  }

  override fun showPermissionWarning(apkFile: ApkFile) {
    ApkWarningDialog(this).init {
      installCallback { presenter.installApk(apkFile) }
      permissionList(apkFile.dangerousPermissions)
    }.show()
  }

  private fun openAboutActivity() {
    startActivity(Intent(this, AboutActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      putExtra(SCROLL_BOTTOM, true)
    })
  }

  private fun showOptions(show: Boolean) {
    binding.deleteHeaderGroup.visibility = if (show) View.VISIBLE else View.GONE
  }

  private fun showConfirmationDialog() {
    ApkDeleteDialog(this).init {
      deleteMessage(adapter.getSelectedCount())
      deleteAction {
        showOptions(false)
        binding.progressbar.visibility = View.VISIBLE
        presenter.deleteApkFiles(adapter.getSelectedApkFilesAndClear())
      }
    }.show()
  }
}