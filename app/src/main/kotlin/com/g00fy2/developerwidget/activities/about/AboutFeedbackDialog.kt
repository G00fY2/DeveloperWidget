package com.g00fy2.developerwidget.activities.about

import android.content.Context
import androidx.appcompat.app.AppCompatDialog
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.databinding.AboutFeedbackDialogBinding

class AboutFeedbackDialog(context: Context) {

  private var binding: AboutFeedbackDialogBinding
  private val dialog = AppCompatDialog(context, R.style.DialogTheme).apply {
    setCancelable(true)
    setCanceledOnTouchOutside(true)
    binding = AboutFeedbackDialogBinding.inflate(layoutInflater)
    setContentView(binding.root)
  }

  fun mailAction(mailIssueAction: () -> Unit): AboutFeedbackDialog {
    binding.mailTextView.setOnClickListener {
      dialog.dismiss()
      mailIssueAction()
    }
    return this
  }

  fun githubAction(githubIssueAction: () -> Unit): AboutFeedbackDialog {
    binding.githubTextView.setOnClickListener {
      dialog.dismiss()
      githubIssueAction()
    }
    return this
  }

  fun init(func: AboutFeedbackDialog.() -> Unit): AppCompatDialog {
    func()
    return dialog
  }
}