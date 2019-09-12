package com.g00fy2.developerwidget.activities.apkinstall

import android.graphics.drawable.InsetDrawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.activities.apkinstall.ApkAdapter.ApkViewHolder
import com.g00fy2.developerwidget.base.BaseAdapter
import com.g00fy2.developerwidget.base.BaseViewHolder
import com.g00fy2.developerwidget.databinding.ApkItemBinding

class ApkAdapter : BaseAdapter<ApkFile, ApkViewHolder>(ApksDiffUtilsCallback()) {

  private var selectedPositions = mutableSetOf<Int>()
  private var onApkClicked: ((ApkFile?) -> Unit) = {}
  private var onApkSelected: ((Int) -> Unit) = {}

  inner class ApkViewHolder(val binding: ApkItemBinding) : BaseViewHolder(binding) {
    fun setSelected(position: Int) {
      selectedPositions.contains(position).let { selected ->
        binding.appIconImageview.visibility = if (selected) View.INVISIBLE else View.VISIBLE
        binding.selectedIconImageview.visibility = if (selected) View.VISIBLE else View.INVISIBLE
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApkViewHolder {
    return ApkViewHolder(ApkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
      addRipple()
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
      binding.filenameTextview.text = apkFile.fileName
      binding.appNameTextview.text = apkFile.appName
      binding.appVersionTextview.text =
        String.format(itemView.context.getString(R.string.apk_version), apkFile.versionName, apkFile.versionCode)
      binding.fileSizeTextview.text = apkFile.size
      binding.appDebugImageview.visibility = if (apkFile.debuggable) View.VISIBLE else View.INVISIBLE
      binding.fileDateTextview.text = apkFile.lastModified
      binding.appIconImageview.setImageDrawable(apkFile.appIcon)
      if (VERSION.SDK_INT >= VERSION_CODES.O) {
        binding.appIconImageview.setBackgroundResource(if (apkFile.appIcon is InsetDrawable) R.drawable.bg_adaptive_launcher_icon else 0)
      }
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

  fun getSelectedCount() = selectedPositions.size

  fun getSelectedApkFilesAndClear(): List<ApkFile> {
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