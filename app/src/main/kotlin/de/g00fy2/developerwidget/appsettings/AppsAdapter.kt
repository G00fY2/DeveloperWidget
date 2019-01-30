package de.g00fy2.developerwidget.appsettings

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.appsettings.AppsAdapter.AppViewHolder
import de.g00fy2.developerwidget.base.BaseAdapter
import de.g00fy2.developerwidget.base.BaseViewHolder
import de.g00fy2.developerwidget.util.FilterUtil
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

  override fun addAll(newItems: MutableList<AppInfo>) {
    itemsCopy.clear()
    itemsCopy.addAll(newItems)
    super.addAll(newItems)
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

  fun updateAppFilter(s: Editable?) {
    if (s == null || s.isEmpty()) {
      resetAppFilter()
    } else {
      items.clear()
      val filterInputs = s.split("*")
      for (i in itemsCopy) {
        if (FilterUtil.valueContainsAllFilters(i.packageName, filterInputs)) items.add(i)
      }
      notifyDataSetChanged()
    }
  }

  fun resetAppFilter() {
    items.clear()
    items.addAll(itemsCopy)
    notifyDataSetChanged()
  }

}