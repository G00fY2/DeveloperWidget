package com.g00fy2.developerwidget.activities.apkinstall.dialogs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.g00fy2.developerwidget.activities.apkinstall.dialogs.PermissionsAdapter.PermissionViewHolder
import com.g00fy2.developerwidget.base.BaseAdapter
import com.g00fy2.developerwidget.base.BaseViewHolder
import com.g00fy2.developerwidget.databinding.PermissionItemBinding

class PermissionsAdapter : BaseAdapter<Pair<String, String?>, PermissionViewHolder>(PermissionsDiffUtilsCallback()) {

  class PermissionViewHolder(val binding: PermissionItemBinding) : BaseViewHolder<Pair<String, String?>>(binding) {
    override fun onBind(item: Pair<String, String?>) {
      item.run {
        binding.permissionTitle.text = item.first
        binding.permissionDescription.text = item.second ?: ""
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    PermissionViewHolder(PermissionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
}

class PermissionsDiffUtilsCallback : DiffUtil.ItemCallback<Pair<String, String?>>() {

  override fun areItemsTheSame(oldItem: Pair<String, String?>, newItem: Pair<String, String?>) = false
  override fun areContentsTheSame(oldItem: Pair<String, String?>, newItem: Pair<String, String?>) = false
}