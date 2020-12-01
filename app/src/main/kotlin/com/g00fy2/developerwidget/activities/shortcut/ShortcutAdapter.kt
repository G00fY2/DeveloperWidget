package com.g00fy2.developerwidget.activities.shortcut

import android.content.pm.ShortcutInfo
import android.os.Build.VERSION_CODES
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import com.g00fy2.developerwidget.activities.shortcut.ShortcutAdapter.ShortcutViewHolder
import com.g00fy2.developerwidget.base.BaseAdapter
import com.g00fy2.developerwidget.base.BaseViewHolder
import com.g00fy2.developerwidget.databinding.ShortcutItemBinding

@RequiresApi(VERSION_CODES.N_MR1)
class ShortcutAdapter : BaseAdapter<ShortcutInfo, ShortcutViewHolder>(ShortcutDiffUtilsCallback()) {

  private var onShortcutSelected: ((Int) -> Unit) = {}

  class ShortcutViewHolder(val binding: ShortcutItemBinding) : BaseViewHolder<ShortcutInfo>(binding) {
    override fun onBind(item: ShortcutInfo) {
      item.run {
        binding.shortcutTitleTextview.text = item.longLabel ?: item.shortLabel ?: ""
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortcutViewHolder {
    return ShortcutViewHolder(ShortcutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
      addRipple()
      itemView.setOnClickListener { onShortcutSelected(bindingAdapterPosition) }
    }
  }

  fun setOnShortcutSelected(onShortcutSelected: (Int) -> Unit) {
    this.onShortcutSelected = onShortcutSelected
  }
}

class ShortcutDiffUtilsCallback : DiffUtil.ItemCallback<ShortcutInfo>() {

  override fun areItemsTheSame(oldItem: ShortcutInfo, newItem: ShortcutInfo) = false
  override fun areContentsTheSame(oldItem: ShortcutInfo, newItem: ShortcutInfo) = false
}
