package com.g00fy2.developerwidget.activities.about.dialogs

import android.content.Context
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialog
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.databinding.AboutSearchDepthDialogBinding

class SearchDepthDialog(context: Context) {

  private val binding: AboutSearchDepthDialogBinding
  private val dialog = AppCompatDialog(context, R.style.DialogNestedTheme).apply {
    setCancelable(true)
    setCanceledOnTouchOutside(true)
    binding = AboutSearchDepthDialogBinding.inflate(layoutInflater)
    setContentView(binding.root)
    binding.dialogCancelTextview.setOnClickListener { dismiss() }
    binding.inputEdittext.requestFocus()
    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
  }

  fun initialValue(depth: Int): SearchDepthDialog {
    binding.inputEdittext.setText(depth.toString())
    binding.inputEdittext.setSelection(depth.toString().length)
    return this
  }

  fun onPositive(onPositiveAction: (Int) -> Unit): SearchDepthDialog {
    binding.dialogOkTextview.setOnClickListener {
      onPositiveAction(binding.inputEdittext.text.toString().toIntOrNull() ?: 2)
      dialog.dismiss()
    }
    return this
  }

  fun init(func: SearchDepthDialog.() -> Unit): AppCompatDialog {
    func()
    return dialog
  }
}