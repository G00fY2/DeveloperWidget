package com.g00fy2.developerwidget.activities.shortcut

import android.annotation.TargetApi
import android.content.pm.ShortcutInfo
import android.os.Build.VERSION_CODES
import android.view.LayoutInflater
import android.view.ViewGroup
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.base.BaseAdapter
import com.g00fy2.developerwidget.base.BaseViewHolder
import kotlinx.android.synthetic.main.shortcut_item.*

class ShortcutAdapter : BaseAdapter<ShortcutInfo, BaseViewHolder>(ShortcutDiffUtilsCallback()) {

  private var onShortcutSelected: ((Int) -> Unit) = {}

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
    return BaseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.shortcut_item, parent, false)).apply {
      addRipple()
      itemView.setOnClickListener { onShortcutSelected(adapterPosition) }
    }
  }

  @TargetApi(VERSION_CODES.N_MR1)
  override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
    holder.apply {
      shortcut_title_textview.text =
        getItem(position)?.longLabel?.toString() ?: getItem(position)?.shortLabel.toString()
    }
  }

  fun setOnShortcutSelected(onShortcutSelected: (Int) -> Unit) {
    this.onShortcutSelected = onShortcutSelected
  }
}
