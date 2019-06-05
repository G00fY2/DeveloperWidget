package com.g00fy2.developerwidget.activities.shortcut

import android.content.pm.ShortcutInfo
import androidx.recyclerview.widget.DiffUtil

class ShortcutDiffUtilsCallback : DiffUtil.ItemCallback<ShortcutInfo>() {

  override fun areItemsTheSame(oldItem: ShortcutInfo, newItem: ShortcutInfo) = false
  override fun areContentsTheSame(oldItem: ShortcutInfo, newItem: ShortcutInfo) = false
}