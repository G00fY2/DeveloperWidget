package com.g00fy2.developerwidget.activities.apkinstall

import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import com.g00fy2.developerwidget.R

class ApkDeleteDialog(context: Context) {

  private val dialog = AppCompatDialog(context, R.style.DialogTheme).apply {
    setCancelable(true)
    setContentView(R.layout.apk_delete_dialog)
    findViewById<TextView>(R.id.dialog_cancel_textview)?.setOnClickListener { dismiss() }
  }

  fun deleteMessage(count: Int): ApkDeleteDialog {
    dialog.findViewById<TextView>(R.id.dialog_message_textview)?.text = if (count > 1) String.format(
      dialog.context.resources.getString(R.string.message_delete_multi),
      count
    ) else dialog.context.resources.getString(R.string.message_delete_single)

    return this
  }

  fun deleteAction(deleteAction: () -> Unit): ApkDeleteDialog {
    dialog.findViewById<TextView>(R.id.dialog_delete_textview)?.setOnClickListener {
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