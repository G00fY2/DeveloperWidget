package com.g00fy2.developerwidget.activities.apkinstall

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.activities.apkinstall.ApkAdapter.ApkViewHolder
import com.g00fy2.developerwidget.base.BaseAdapter
import com.g00fy2.developerwidget.base.BaseViewHolder
import kotlinx.android.synthetic.main.apk_item.*
import kotlinx.android.synthetic.main.apk_item.view.*

class ApkAdapter : BaseAdapter<ApkFile, ApkViewHolder>(ApksDiffUtilsCallback()) {

  private var selectedPositions = mutableSetOf<Int>()
  private var onApkClicked: ((ApkFile?) -> Unit) = {}
  private var onApkSelected: ((Int) -> Unit) = {}

  inner class ApkViewHolder(containerView: View) : BaseViewHolder(containerView) {
    fun setSelected(position: Int) {
      itemView.selected_icon_imageview.visibility =
        if (selectedPositions.contains(position)) View.VISIBLE else View.INVISIBLE
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApkViewHolder {
    return ApkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.apk_item, parent, false)).apply {
      itemView.setOnClickListener {
        adapterPosition.let {
          if (selectedPositions.isNotEmpty()) {
            if (selectedPositions.contains(it)) {
              selectedPositions.remove(it)
            } else {
              selectedPositions.add(it)
            }
            notifyItemChanged(it, true)
            onApkSelected(selectedPositions.size)
          } else {
            onApkClicked(getSelectedFile(it))
          }
        }
      }
      itemView.setOnLongClickListener {
        adapterPosition.let {
          if (selectedPositions.contains(it)) {
            selectedPositions.remove(it)
          } else {
            selectedPositions.add(it)
          }
          notifyItemChanged(it, true)
          onApkSelected(selectedPositions.size)
          true
        }
      }
    }
  }

  override fun onBindViewHolder(holderApk: ApkViewHolder, position: Int) {
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
      setSelected(position)
    }
  }

  override fun onBindViewHolder(holderApk: ApkViewHolder, position: Int, payloads: List<Any>) {
    if (payloads.isEmpty()) {
      onBindViewHolder(holderApk, position)
    } else {
      holderApk.setSelected(position)
    }
  }

  override fun clearList() {
    super.clearList()
    selectedPositions.clear()
  }

  fun setOnApkClicked(onApkClicked: (ApkFile?) -> Unit) {
    this.onApkClicked = onApkClicked
  }

  fun setOnApkSelect(onApkSelected: (Int) -> Unit) {
    this.onApkSelected = onApkSelected
  }

  fun clearSelectedList() {
    val listCopy = selectedPositions.toList()
    selectedPositions.clear()
    for (i in listCopy) {
      notifyItemChanged(i, true)
    }
  }

  fun getSelectedApkFiles(): List<ApkFile> {
    val selectedFiles = mutableListOf<ApkFile>()
    for (position in selectedPositions) {
      selectedFiles.add(getItem(position))
    }
    selectedPositions.clear()
    return selectedFiles
  }

  private fun getSelectedFile(position: Int): ApkFile? {
    return if (position != RecyclerView.NO_POSITION && position < itemCount) {
      getItem(position)
    } else {
      null
    }
  }
}