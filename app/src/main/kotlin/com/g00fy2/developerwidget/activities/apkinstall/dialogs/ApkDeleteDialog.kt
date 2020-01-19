package com.g00fy2.developerwidget.activities.apkinstall.dialogs

import android.content.Context
import androidx.appcompat.app.AppCompatDialog
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.databinding.ApkDeleteDialogBinding

class ApkDeleteDialog(context: Context) {

  private val binding: ApkDeleteDialogBinding
  private val dialog = AppCompatDialog(context, R.style.DialogTheme).apply {
    setCancelable(true)
    binding = ApkDeleteDialogBinding.inflate(layoutInflater)
    setContentView(binding.root)
    binding.dialogCancelTextview.setOnClickListener { dismiss() }
  }

  fun deleteMessage(count: Int): ApkDeleteDialog {
    binding.dialogMessageTextview.text = if (count > 1) String.format(
      dialog.context.resources.getString(R.string.message_delete_multi),
      count
    ) else dialog.context.resources.getString(R.string.message_delete_single)

    return this
  }

  fun deleteAction(deleteAction: () -> Unit): ApkDeleteDialog {
    binding.dialogDeleteTextview.setOnClickListener {
      dialog.dismiss()
      deleteAction()
    }
    return this
  }

  fun init(func: ApkDeleteDialog.() -> Unit): AppCompatDialog {
    func()
    return dialog
  }
}