package com.g00fy2.developerwidget.activities.apkinstall

import android.app.Dialog
import android.content.Context
import android.widget.TextView
import com.g00fy2.developerwidget.R

class ApkDeleteDialog(private val context: Context) {

  private val dialog = Dialog(context, R.style.DialogTheme).apply {
    setCancelable(true)
    setContentView(R.layout.apk_delete_dialog)
    findViewById<TextView>(R.id.dialog_cancel_textview).setOnClickListener { dismiss() }
  }

  fun deleteMessage(amount: Int): ApkDeleteDialog {
    dialog.findViewById<TextView>(R.id.dialog_message_textview).text =
      if (amount > 1) String.format(
        context.resources.getString(R.string.message_delete_multi),
        amount
      ) else context.resources.getString(R.string.message_delete_single)

    return this
  }

  fun deleteAction(deleteAction: () -> Unit): ApkDeleteDialog {
    dialog.findViewById<TextView>(R.id.dialog_delete_textview).setOnClickListener {
      dialog.dismiss()
      deleteAction()
    }
    return this
  }

  fun init(func: ApkDeleteDialog.() -> Unit): Dialog {
    func()
    return dialog
  }
}