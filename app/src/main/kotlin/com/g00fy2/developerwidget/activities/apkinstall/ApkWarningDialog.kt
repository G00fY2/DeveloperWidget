package com.g00fy2.developerwidget.activities.apkinstall

import android.content.Context
import androidx.appcompat.app.AppCompatDialog
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.databinding.ApkWarningDialogBinding

class ApkWarningDialog(context: Context) {

  private var binding: ApkWarningDialogBinding
  private val dialog = AppCompatDialog(context, R.style.DialogTheme).apply {
    setCancelable(true)
    binding = ApkWarningDialogBinding.inflate(layoutInflater)
    setContentView(binding.root)
    binding.dialogCancelTextview.setOnClickListener { dismiss() }
  }

  fun deleteMessage(count: Int): ApkWarningDialog {
    binding.dialogMessageTextview.text = if (count > 1) String.format(
      dialog.context.resources.getString(R.string.message_delete_multi),
      count
    ) else dialog.context.resources.getString(R.string.message_delete_single)

    return this
  }

  fun deleteAction(deleteAction: () -> Unit): ApkWarningDialog {
    binding.dialogInstallTextview.setOnClickListener {
      dialog.dismiss()
      deleteAction()
    }
    return this
  }

  fun init(func: ApkWarningDialog.() -> Unit): AppCompatDialog {
    func()
    return dialog
  }
}