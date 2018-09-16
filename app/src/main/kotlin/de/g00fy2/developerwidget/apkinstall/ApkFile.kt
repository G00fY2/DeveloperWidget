package de.g00fy2.developerwidget.apkinstall

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import java.io.File

class ApkFile(var file: File, pm: PackageManager) : Comparable<ApkFile> {

  var fileName: String
  var apkIcon: Drawable

  init {
    val pi = pm.getPackageArchiveInfo(file.absolutePath, 0).applicationInfo
    pi.sourceDir = file.absolutePath
    pi.publicSourceDir = file.absolutePath
    fileName = file.nameWithoutExtension
    apkIcon = pi.loadIcon(pm)
  }

  override fun compareTo(other: ApkFile): Int {
    return when {
      file.lastModified() > other.file.lastModified() -> -1
      file.lastModified() < other.file.lastModified() -> 1
      else -> 0
    }
  }

}