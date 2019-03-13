package com.g00fy2.developerwidget.activities.appmanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.base.BaseAdapter
import com.g00fy2.developerwidget.base.BaseViewHolder
import com.g00fy2.developerwidget.utils.FilterUtils
import kotlinx.android.synthetic.main.app_item.*

class AppsAdapter : BaseAdapter<AppInfo, BaseViewHolder>(AppsDiffUtilsCallback()) {

  private var onAppClicked: ((AppInfo?) -> Unit) = {}
  private var itemsCopy = ArrayList<AppInfo>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
    return BaseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.app_item, parent, false)).apply {
      itemView.setOnClickListener {
        onAppClicked(getSelectedPackageName(adapterPosition))
      }
    }
  }

  override fun onBindViewHolder(holderApk: BaseViewHolder, position: Int) {
    holderApk.apply {
      val appInfo = getItem(position)
      appename_textview.text = appInfo.appName
      apppackage_textview.text = appInfo.packageName
      app_version_textview.text =
        String.format(itemView.context.getString(R.string.apk_version), appInfo.versionName, appInfo.versionCode)
      app_icon_imageview.setImageDrawable(appInfo.appIcon)
    }
  }

  fun initialList(list: List<AppInfo>?, filters: Collection<String>) {
    itemsCopy.clear()
    list?.let { itemsCopy.addAll(it) }
    updateAppFilters(filters)
  }

  fun setOnAppClicked(onAppClicked: (AppInfo?) -> Unit): AppsAdapter {
    this.onAppClicked = onAppClicked
    return this
  }

  fun updateAppFilter(filter: String) {
    if (filter.isEmpty()) {
      resetAppFilter()
    } else {
      val filteredItems = ArrayList<AppInfo>()
      for (i in itemsCopy) {
        if (FilterUtils.filterValue(i.packageName, filter)) filteredItems.add(i)
      }
      submitList(filteredItems)
    }
  }

  fun updateAppFilters(filters: Collection<String>) {
    if (filters.isEmpty()) {
      resetAppFilter()
    } else {
      val filteredItems = ArrayList<AppInfo>()
      for (i in itemsCopy) {
        if (FilterUtils.filterValueByCollection(i.packageName, filters)) filteredItems.add(i)
      }
      submitList(filteredItems)
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