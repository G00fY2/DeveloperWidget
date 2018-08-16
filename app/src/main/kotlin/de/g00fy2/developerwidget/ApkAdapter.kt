package de.g00fy2.developerwidget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.apk_item.view.app_icon_imageview
import kotlinx.android.synthetic.main.apk_item.view.filename_textview

class ApkAdapter(apkActivity: ApkActivity) : RecyclerView.Adapter<ApkAdapter.ViewHolder>() {

  private var listener = apkActivity
  private var apkFiles: MutableList<ApkFile> = ArrayList()
  private var selectedFile: ApkFile? = null

  class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApkAdapter.ViewHolder {
    return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.apk_item, parent, false))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val file: ApkFile = apkFiles[position]
    holder.view.setOnClickListener {
      listener.fileSelected()
      selectedFile = file
      notifyDataSetChanged()
    }
    holder.view.filename_textview.text = file.fileName
    holder.view.app_icon_imageview.setImageDrawable(file.apkIcon)
    animateSelectedIcon(holder.view.app_icon_imageview, file == selectedFile)
  }

  override fun getItemCount() = apkFiles.size

  fun clear() {
    apkFiles.clear()
    selectedFile = null
    notifyDataSetChanged()
  }

  fun add(apkFile: ApkFile) {
    apkFiles.add(apkFile)
    notifyDataSetChanged()
  }

  fun getSelectedFile(): ApkFile? {
    return selectedFile
  }

  private fun animateSelectedIcon(imageView: ImageView, selected: Boolean) {
    if (imageView.isSelected != selected) {
      imageView.isSelected = selected
      imageView.animate().scaleX(if (selected) 1.5f else 1f).scaleY(if (selected) 1.5f else 1f).setDuration(300).start()
    }
  }
}