package de.g00fy2.developerwidget.appsettings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.appsettings.AppsAdapter.AppViewHolder
import de.g00fy2.developerwidget.base.BaseAdapter
import de.g00fy2.developerwidget.base.BaseViewHolder
import de.g00fy2.developerwidget.util.FilterUtils
import kotlinx.android.synthetic.main.app_item.*

class AppsAdapter : BaseAdapter<AppInfo, AppViewHolder>() {

  private var selectedPosition = RecyclerView.NO_POSITION
  private var onAppSelected: (() -> Unit) = {}
  private var itemsCopy = ArrayList<AppInfo>()

  inner class AppViewHolder(containerView: View) : BaseViewHolder(containerView)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
    return AppViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.app_item, parent, false)).apply {
      itemView.setOnClickListener {
        selectedPosition = adapterPosition
        onAppSelected()
      }
    }
  }

  override fun onBindViewHolder(holderApk: AppViewHolder, position: Int) {
    holderApk.apply {
      val appInfo = getItem(position)
      appename_textview.text = appInfo.appName
      apppackage_textview.text = appInfo.packageName
      app_version_textview.text =
        String.format(itemView.context.getString(R.string.apk_version), appInfo.versionName, appInfo.versionCode)
      app_icon_imageview.setImageDrawable(appInfo.appIcon)
    }
  }

  override fun clear() {
    super.clear()
    selectedPosition = RecyclerView.NO_POSITION
  }

  override fun setItems(newItems: MutableList<AppInfo>) {
    itemsCopy.clear()
    itemsCopy.addAll(newItems)
    super.setItems(newItems)
  }

  fun getSelectedPackageName(): String? {
    return if (selectedPosition != RecyclerView.NO_POSITION && selectedPosition < itemCount) {
      getItem(selectedPosition).packageName
    } else {
      null
    }
  }

  fun setOnAppSelected(onAppSelected: () -> Unit): AppsAdapter {
    this.onAppSelected = onAppSelected
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
      setItems(filteredItems, DiffUtil.calculateDiff((AppsDiffUtilsCallback(items, filteredItems))))
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
      setItems(filteredItems, DiffUtil.calculateDiff((AppsDiffUtilsCallback(items, filteredItems))))
    }
  }

  private fun resetAppFilter() {
    setItems(itemsCopy, DiffUtil.calculateDiff((AppsDiffUtilsCallback(items, itemsCopy))))
  }

}