package com.g00fy2.developerwidget.activities.apkinstall.dialogs

import android.content.Context
import androidx.appcompat.app.AppCompatDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.databinding.ApkWarningDialogBinding

class ApkWarningDialog(context: Context) {

  private val binding: ApkWarningDialogBinding
  private val adapter: PermissionsAdapter
  private var installAction: () -> Unit = {}
  private val dialog = AppCompatDialog(context, R.style.DialogNestedTheme).apply {
    setCancelable(true)
    binding = ApkWarningDialogBinding.inflate(layoutInflater)
    setContentView(binding.root)
    binding.dialogCancelTextview.setOnClickListener { dismiss() }
    binding.dialogInstallTextview.setOnClickListener {
      dismiss()
      installAction()
    }

    adapter = PermissionsAdapter()
    binding.recyclerview.setHasFixedSize(true)
    binding.recyclerview.layoutManager = LinearLayoutManager(context)
    binding.recyclerview.adapter = adapter
  }

  fun permissionList(permissions: List<Pair<String, String?>>): ApkWarningDialog {
    adapter.submitList(permissions)
    return this
  }

  fun installCallback(action: () -> Unit): ApkWarningDialog {
    installAction = action
    return this
  }

  fun init(func: ApkWarningDialog.() -> Unit): AppCompatDialog {
    func()
    return dialog
  }
}