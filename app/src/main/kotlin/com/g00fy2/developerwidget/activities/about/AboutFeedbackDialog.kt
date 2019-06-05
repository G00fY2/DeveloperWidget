package com.g00fy2.developerwidget.activities.about

import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import com.g00fy2.developerwidget.R

class AboutFeedbackDialog(context: Context) {

  private val dialog = AppCompatDialog(context, R.style.DialogTheme).apply {
    setCancelable(true)
    setCanceledOnTouchOutside(true)
    setContentView(R.layout.about_feedback_dialog)
  }

  fun mailAction(mailIssueAction: () -> Unit): AboutFeedbackDialog {
    dialog.findViewById<TextView>(R.id.mailTextView)?.setOnClickListener {
      dialog.dismiss()
      mailIssueAction()
    }
    return this
  }

  fun githubAction(githubIssueAction: () -> Unit): AboutFeedbackDialog {
    dialog.findViewById<TextView>(R.id.githubTextView)?.setOnClickListener {
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