package de.g00fy2.developerwidget.apkinstall

import android.content.Context
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

class ApkAdapter(private var context: Context) : RecyclerView.Adapter<ViewHolder>() {

  private var apkFiles: MutableList<ApkFile> = ArrayList()
  private var selectedPosition = -1
  private var apkSelectedListener: (() -> Unit) = {}

  class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val filename: TextView = view.filename_textview
    val fileDate: TextView = view.file_date_textview
    val fileAppName: TextView = view.file_appname_textview
    val fileVersion: TextView = view.file_version_textview
    val fileSize: TextView = view.file_size_textview
    val appIcon: ImageView = view.app_icon_imageview
    val debugIcon: ImageView = view.file_debug_imageview

    fun setBackground(selected: Boolean) {
      itemView.setBackgroundResource(if (selected) R.color.transparentAccent else android.R.color.transparent)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val holder = ViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.apk_item, parent, false)
    )
    holder.itemView.setOnClickListener {
      val adapterPosition = holder.adapterPosition
      if (adapterPosition != RecyclerView.NO_POSITION && adapterPosition != selectedPosition) {
        if (selectedPosition < 0) {
          apkSelectedListener()
        } else {
          notifyItemChanged(selectedPosition, APK_DESELECTED)
        }
        selectedPosition = adapterPosition
        notifyItemChanged(selectedPosition, APK_SELECTED)
      }
    }
    return holder
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val apkFile: ApkFile = apkFiles[position]
    holder.filename.text = apkFile.getFileName()
    holder.fileAppName.text = apkFile.getAppName(context)
    holder.fileVersion.text =
        String.format(context.getString(R.string.apk_version), apkFile.getVersionName(), apkFile.getVersionCode())
    holder.fileSize.text = apkFile.getSize()
    holder.debugIcon.visibility = if (apkFile.isDebuggableApp()) View.VISIBLE else View.INVISIBLE
    holder.fileDate.text = apkFile.getLastModified(context)
    holder.appIcon.setImageDrawable(apkFile.getIcon(context))
    holder.setBackground(position == selectedPosition)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: List<Any>) {
    when {
      payloads.isEmpty() -> {
        onBindViewHolder(holder, position)
      }
      payloads.contains(APK_SELECTED) -> {
        holder.setBackground(true)
      }
      payloads.contains(APK_DESELECTED) -> {
        holder.setBackground(false)
      }
    }
  }

  override fun getItemCount() = apkFiles.size

  fun setOnApkSelectedListener(listener: () -> Unit) {
    apkSelectedListener = listener
  }

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
    return if (selectedPosition >= 0 && selectedPosition < apkFiles.size) {
      apkFiles[selectedPosition]
    } else {
      null
    }
  }

  companion object {
    const val APK_DESELECTED = 0
    const val APK_SELECTED = 1
  }
}