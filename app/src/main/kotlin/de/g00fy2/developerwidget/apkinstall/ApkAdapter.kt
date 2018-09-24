package de.g00fy2.developerwidget.apkinstall

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.apkinstall.ApkAdapter.ViewHolder
import kotlinx.android.synthetic.main.apk_item.view.app_icon_imageview
import kotlinx.android.synthetic.main.apk_item.view.file_appname_textview
import kotlinx.android.synthetic.main.apk_item.view.file_date_textview
import kotlinx.android.synthetic.main.apk_item.view.file_debug_imageview
import kotlinx.android.synthetic.main.apk_item.view.file_size_textview
import kotlinx.android.synthetic.main.apk_item.view.file_version_textview
import kotlinx.android.synthetic.main.apk_item.view.filename_textview

class ApkAdapter(private var apkActivity: ApkActivity) : RecyclerView.Adapter<ViewHolder>() {

  private var apkFiles: MutableList<ApkFile> = ArrayList()
  private var selectedPosition = -1

  class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val filename: TextView = view.filename_textview
    val fileDate: TextView = view.file_date_textview
    val fileAppName: TextView = view.file_appname_textview
    val fileVersion: TextView = view.file_version_textview
    val fileSize: TextView = view.file_size_textview
    val appIcon: ImageView = view.app_icon_imageview
    val debugIcon: ImageView = view.file_debug_imageview
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.apk_item, parent, false)
    )
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.view.setOnClickListener {
      if (selectedPosition < 0) {
        apkActivity.fileSelected()
      } else {
        notifyItemChanged(selectedPosition)
      }
      selectedPosition = position
      notifyItemChanged(position)
    }

    val apkFile: ApkFile = apkFiles[position]
    holder.filename.text = apkFile.getFileName()
    holder.fileAppName.text = apkFile.getAppName(apkActivity)
    holder.fileVersion.text =
        String.format(apkActivity.getString(R.string.apk_version), apkFile.getVersionName(), apkFile.getVersionCode())
    holder.fileSize.text = apkFile.getSize()
    holder.debugIcon.visibility = if (apkFile.isDebuggableApp()) View.VISIBLE else View.INVISIBLE
    holder.fileDate.text = apkFile.getLastModified(apkActivity)
    holder.appIcon.setImageDrawable(apkFile.getIcon(apkActivity))

    val selected = position == selectedPosition
    holder.filename.setTypeface(null, if (selected) Typeface.BOLD else Typeface.NORMAL)
    holder.appIcon.scaleX = if (selected) 1.5f else 1f
    holder.appIcon.scaleY = if (selected) 1.5f else 1f
  }

  override fun getItemCount() = apkFiles.size

  fun clear() {
    apkFiles.clear()
    selectedPosition = -1
    notifyDataSetChanged()
  }

  fun add(apkFile: ApkFile) {
    apkFiles.add(apkFile)
    apkFiles.sort()
    notifyDataSetChanged()
  }

  fun addAll(apkFiles: MutableList<ApkFile>) {
    this.apkFiles = apkFiles
    apkFiles.sort()
    notifyDataSetChanged()
  }

  fun getSelectedFile(): ApkFile? {
    return if (selectedPosition >= 0) {
      apkFiles[selectedPosition]
    } else {
      null
    }
  }
}