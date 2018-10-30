package de.g00fy2.developerwidget.apkinstall

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.apkinstall.ApkAdapter.ViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.apk_item.*

class ApkAdapter : RecyclerView.Adapter<ViewHolder>() {

  private var apkFiles: MutableList<ApkFile> = ArrayList(0)
  private var selectedPosition = RecyclerView.NO_POSITION
  private var onApkSelected: (() -> Unit) = {}

  inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun setBackground(position: Int) {
      itemView.setBackgroundResource(if (position == selectedPosition) R.color.transparentAccent else 0)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.apk_item, parent, false)).apply {
      itemView.setOnClickListener {
        if (adapterPosition != RecyclerView.NO_POSITION && adapterPosition != selectedPosition) {
          if (selectedPosition == RecyclerView.NO_POSITION) {
            onApkSelected()
          } else {
            notifyItemChanged(selectedPosition, true)
          }
          selectedPosition = adapterPosition
          notifyItemChanged(selectedPosition, true)
        }
      }
    }
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.apply {
      val apkFile = apkFiles[position]
      filename_textview.text = apkFile.fileName
      app_name_textview.text = apkFile.appName
      app_version_textview.text =
          String.format(itemView.context.getString(R.string.apk_version), apkFile.versionName, apkFile.versionCode)
      file_size_textview.text = apkFile.size
      app_debug_imageview.visibility = if (apkFile.debuggable) View.VISIBLE else View.INVISIBLE
      file_date_textview.text = apkFile.lastModified
      app_icon_imageview.setImageDrawable(apkFile.appIcon)
      setBackground(position)
    }
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: List<Any>) {
    if (payloads.isEmpty()) {
      onBindViewHolder(holder, position)
    } else {
      holder.setBackground(position)
    }
  }

  override fun getItemCount() = apkFiles.size

  fun setOnApkSelected(onApkSelected: () -> Unit) = run { this.onApkSelected = onApkSelected }

  fun clear() {
    apkFiles.clear()
    selectedPosition = RecyclerView.NO_POSITION
    notifyDataSetChanged()
  }

  fun addAll(apkFiles: MutableList<ApkFile>) {
    this.apkFiles = apkFiles
    notifyDataSetChanged()
  }

  fun getSelectedFile(): ApkFile? {
    return if (selectedPosition != RecyclerView.NO_POSITION && selectedPosition < itemCount) {
      apkFiles[selectedPosition]
    } else {
      null
    }
  }
}