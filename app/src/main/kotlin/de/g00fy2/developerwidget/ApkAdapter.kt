package de.g00fy2.developerwidget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.apk_item.view.filename_textview
import kotlinx.android.synthetic.main.apk_item.view.radio_button
import java.io.File


class ApkAdapter(apkActivity: ApkActivity) : RecyclerView.Adapter<ApkAdapter.ViewHolder>() {

  private var listener = apkActivity
  private var apkFiles: MutableList<File> = ArrayList()
  private var selectedFile: File? = null

  class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApkAdapter.ViewHolder {
    return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.apk_item, parent, false))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val file: File = apkFiles[position]
    holder.view.setOnClickListener {
      listener.fileSelected()
      selectedFile = file
      notifyDataSetChanged()
    }
    holder.view.radio_button.isChecked = file == selectedFile
    holder.view.filename_textview.text = file.name
  }

  override fun getItemCount() = apkFiles.size

  fun clear() {
    apkFiles.clear()
    selectedFile = null
    notifyDataSetChanged()
  }

  fun add(apkFile: File) {
    apkFiles.add(apkFile)
    notifyDataSetChanged()
  }

  fun getSelectedFile(): File? {
    return selectedFile
  }
}