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
import kotlinx.android.synthetic.main.apk_item.view.filename_textview


class ApkAdapter(apkActivity: ApkActivity) : RecyclerView.Adapter<ViewHolder>() {

  private var listener = apkActivity
  private var apkFiles: MutableList<ApkFile> = ArrayList()
  private var selectedPosition = -1

  class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val filename: TextView = view.filename_textview
    val appIcon: ImageView = view.app_icon_imageview
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.apk_item, parent, false))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.view.setOnClickListener {
      if (selectedPosition < 0) {
        listener.fileSelected()
      } else {
        notifyItemChanged(selectedPosition)
      }
      selectedPosition = position
      notifyItemChanged(position)
    }

    val file: ApkFile = apkFiles[position]
    holder.filename.text = file.fileName
    holder.appIcon.setImageDrawable(file.apkIcon)
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

  fun getSelectedFile(): ApkFile? {
    if (selectedPosition >= 0) {
      return apkFiles[selectedPosition]
    }
    return null
  }
}