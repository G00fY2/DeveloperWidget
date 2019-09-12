package com.g00fy2.developerwidget.activities.shortcut

import android.content.pm.ShortcutInfo
import android.os.Build.VERSION_CODES
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.g00fy2.developerwidget.activities.shortcut.ShortcutAdapter.ShortcutViewHolder
import com.g00fy2.developerwidget.base.BaseAdapter
import com.g00fy2.developerwidget.base.BaseViewHolder
import com.g00fy2.developerwidget.databinding.ShortcutItemBinding

class ShortcutAdapter : BaseAdapter<ShortcutInfo, ShortcutViewHolder>(ShortcutDiffUtilsCallback()) {

  private var onShortcutSelected: ((Int) -> Unit) = {}

  inner class ShortcutViewHolder(val binding: ShortcutItemBinding) : BaseViewHolder(binding)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortcutViewHolder {
    return ShortcutViewHolder(ShortcutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
      addRipple()
      itemView.setOnClickListener { onShortcutSelected(adapterPosition) }
    }
  }

  @RequiresApi(VERSION_CODES.N_MR1)
  override fun onBindViewHolder(holder: ShortcutViewHolder, position: Int) {
    holder.apply {
      binding.shortcutTitleTextview.text =
        getItem(position)?.longLabel?.toString() ?: getItem(position)?.shortLabel.toString()
    }
  }

  fun setOnShortcutSelected(onShortcutSelected: (Int) -> Unit) {
    this.onShortcutSelected = onShortcutSelected
  }
}
