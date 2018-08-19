package de.g00fy2.developerwidget

import android.graphics.drawable.Drawable
import java.io.File

class ApkFile(var file: File) : Comparable<ApkFile> {

  lateinit var fileName: String
  lateinit var apkIcon: Drawable

  override fun compareTo(other: ApkFile): Int {
    return when {
      file.lastModified() > other.file.lastModified() -> -1
      file.lastModified() < other.file.lastModified() -> 1
      else -> 0
    }
  }

}