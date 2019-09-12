package com.g00fy2.developerwidget.activities.appmanager

import android.graphics.drawable.InsetDrawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.activities.appmanager.AppsAdapter.AppViewHolder
import com.g00fy2.developerwidget.base.BaseAdapter
import com.g00fy2.developerwidget.base.BaseViewHolder
import com.g00fy2.developerwidget.databinding.AppItemBinding
import com.g00fy2.developerwidget.ktx.filterPackageName

class AppsAdapter : BaseAdapter<AppInfo, AppViewHolder>(AppsDiffUtilsCallback()) {

  private var onAppClicked: ((AppInfo?) -> Unit) = {}
  private var itemsCopy = ArrayList<AppInfo>()

  inner class AppViewHolder(val binding: AppItemBinding) : BaseViewHolder(binding)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
    return AppViewHolder(AppItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
      addRipple()
      itemView.setOnClickListener {
        onAppClicked(getSelectedPackageName(adapterPosition))
      }
    }
  }

  override fun onBindViewHolder(holderApk: AppViewHolder, position: Int) {
    holderApk.apply {
      val appInfo = getItem(position)
      binding.appenameTextview.text = appInfo.appName
      binding.apppackageTextview.text = appInfo.packageName
      binding.appVersionTextview.text =
        String.format(itemView.context.getString(R.string.apk_version), appInfo.versionName, appInfo.versionCode)
      binding.appIconImageview.setImageDrawable(appInfo.appIcon)
      if (VERSION.SDK_INT >= VERSION_CODES.O) {
        binding.appIconImageview.setBackgroundResource(if (appInfo.appIcon is InsetDrawable) R.drawable.bg_adaptive_launcher_icon else 0)
      }
    }
  }

  fun initialList(list: List<AppInfo>?, filters: Collection<String>) {
    itemsCopy.clear()
    list?.let { itemsCopy.addAll(it) }
    updateAppFilters(filters)
  }

  fun setOnAppClicked(onAppClicked: (AppInfo?) -> Unit) {
    this.onAppClicked = onAppClicked
  }

  fun updateAppFilter(filter: String) {
    if (filter.isEmpty()) {
      resetAppFilter()
    } else {
      submitList(itemsCopy.filter { it.filterPackageName(filter) })
    }
  }

  fun updateAppFilters(filters: Collection<String>) {
    if (filters.isEmpty()) {
      resetAppFilter()
    } else {
      submitList(itemsCopy.filter { it.filterPackageName(filters) })
    }
  }

  private fun getSelectedPackageName(position: Int): AppInfo? {
    return if (position != RecyclerView.NO_POSITION && position < itemCount) {
      getItem(position)
    } else {
      null
    }
  }

  private fun resetAppFilter() {
    if (itemCount != itemsCopy.size) {
      submitList(itemsCopy)
    }
  }
}