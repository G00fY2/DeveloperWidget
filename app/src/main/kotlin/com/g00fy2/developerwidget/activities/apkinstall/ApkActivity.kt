package com.g00fy2.developerwidget.activities.apkinstall

import android.view.View
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.activities.apkinstall.dialogs.ApkDeleteDialog
import com.g00fy2.developerwidget.activities.apkinstall.dialogs.ApkWarningDialog
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.base.BaseContract.BasePresenter
import com.g00fy2.developerwidget.databinding.ActivityApkBinding
import javax.inject.Inject

class ApkActivity : BaseActivity(true), ApkContract.ApkView {

  @Inject
  lateinit var presenter: ApkContract.ApkPresenter

  private lateinit var adapter: ApkAdapter
  private lateinit var binding: ActivityApkBinding

  override fun providePresenter(): BasePresenter = presenter

  override fun setViewBinding(): ViewBinding {
    binding = ActivityApkBinding.inflate(layoutInflater)
    return binding
  }

  override fun initView() {
    adapter = ApkAdapter()
    adapter.setOnApkClicked { apkFile -> presenter.installOrShowPermissionWarning(apkFile) }
    adapter.setOnApkSelect { selectedCount -> showOptions(selectedCount > 0) }
    adapter.setCommitCallback(Runnable {
      adapter.itemCount.let {
        binding.recyclerview.overScrollMode = if (it == 0) View.OVER_SCROLL_NEVER else View.OVER_SCROLL_ALWAYS
        binding.noItemsTextview.visibility = if (it == 0) View.VISIBLE else View.INVISIBLE
        binding.noItemsImageview.visibility = if (it == 0) View.VISIBLE else View.INVISIBLE
      }
    })
    binding.recyclerview.setHasFixedSize(true)
    binding.recyclerview.layoutManager = LinearLayoutManager(this)
    binding.recyclerview.adapter = adapter

    binding.cancelTextview.setOnClickListener { finish() }
    TooltipCompat.setTooltipText(binding.clearImageview, binding.clearImageview.contentDescription)
    binding.deleteImageview.setOnClickListener { showConfirmationDialog() }
    TooltipCompat.setTooltipText(binding.clearImageview, binding.clearImageview.contentDescription)
    binding.clearImageview.setOnClickListener {
      adapter.clearSelectedList()
      showOptions(false)
    }
  }

  override fun onResume() {
    super.onResume()
    if (adapter.itemCount == 0) {
      binding.progressbar.visibility = View.VISIBLE
      binding.noItemsImageview.visibility = View.INVISIBLE
      binding.noItemsTextview.text = getString(R.string.scanning_apks)
      binding.noItemsTextview.visibility = View.VISIBLE
    }
  }

  override fun toggleResultView(apkFiles: List<ApkFile>, missingPermissions: Boolean) {
    if (missingPermissions) {
      binding.progressbar.visibility = View.GONE
      binding.noItemsTextview.text = getString(R.string.missing_permissions)
    } else {
      binding.noItemsTextview.visibility = View.INVISIBLE
      binding.noItemsTextview.text = getString(R.string.no_apk_found)
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