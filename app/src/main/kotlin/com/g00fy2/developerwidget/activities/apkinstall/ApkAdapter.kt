package com.g00fy2.developerwidget.activities.apkinstall

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.base.BaseAdapter
import com.g00fy2.developerwidget.base.BaseViewHolder
import kotlinx.android.synthetic.main.apk_item.*

class ApkAdapter : BaseAdapter<ApkFile, BaseViewHolder>() {

  private var selectedPosition = RecyclerView.NO_POSITION
  private var onApkSelected: (() -> Unit) = {}

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
    return BaseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.apk_item, parent, false)).apply {
      itemView.setOnClickListener {
        selectedPosition = adapterPosition
        onApkSelected()
      }
    }
  }

  override fun onBindViewHolder(holderApk: BaseViewHolder, position: Int) {
    holderApk.apply {
      val apkFile = getItem(position)
      filename_textview.text = apkFile.fileName
      app_name_textview.text = apkFile.appName
      app_version_textview.text =
        String.format(itemView.context.getString(R.string.apk_version), apkFile.versionName, apkFile.versionCode)
      file_size_textview.text = apkFile.size
      app_debug_imageview.visibility = if (apkFile.debuggable) View.VISIBLE else View.INVISIBLE
      file_date_textview.text = apkFile.lastModified
      app_icon_imageview.setImageDrawable(apkFile.appIcon)
    }
  }

  override fun clearList() {
    super.clearList()
    selectedPosition = RecyclerView.NO_POSITION
  }

  fun setOnApkSelected(onApkSelected: () -> Unit): ApkAdapter {
    this.onApkSelected = onApkSelected
    return this
  }

  fun getSelectedFile(): ApkFile? {
    return if (selectedPosition != RecyclerView.NO_POSITION && selectedPosition < itemCount) {
      getItem(selectedPosition)
    } else {
      null
    }
  }
}