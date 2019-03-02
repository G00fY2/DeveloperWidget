package de.g00fy2.developerwidget.activities.appsettings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.base.BaseAdapter
import de.g00fy2.developerwidget.base.BaseViewHolder
import de.g00fy2.developerwidget.utils.FilterUtils
import kotlinx.android.synthetic.main.app_item.*

class AppsAdapter : BaseAdapter<AppInfo, BaseViewHolder>(AppsDiffUtilsCallback()) {

  private var selectedPosition = RecyclerView.NO_POSITION
  private var onAppSelected: (() -> Unit) = {}
  private var itemsCopy = ArrayList<AppInfo>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
    return BaseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.app_item, parent, false)).apply {
      itemView.setOnClickListener {
        selectedPosition = adapterPosition
        onAppSelected()
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

  fun initialList(list: List<AppInfo>?) {
    itemsCopy.clear()
    list?.let { itemsCopy.addAll(it) }
    submitList(list)
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

  private fun resetAppFilter() {
    if (itemCount != itemsCopy.size) {
      submitList(itemsCopy)
    }
  }

}