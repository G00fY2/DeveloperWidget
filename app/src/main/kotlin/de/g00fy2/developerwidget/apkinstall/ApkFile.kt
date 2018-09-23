package de.g00fy2.developerwidget.apkinstall

import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.format.DateFormat
import androidx.core.content.FileProvider
import java.io.File
import java.util.Date
import kotlin.math.round

class ApkFile(private var file: File, context: Context) : Comparable<ApkFile> {

  private var packageInfo: PackageInfo = context.packageManager.getPackageArchiveInfo(file.absolutePath, 0)

  init {
    packageInfo.applicationInfo.sourceDir = file.absolutePath
    packageInfo.applicationInfo.publicSourceDir = file.absolutePath
  }

  fun getFileName(): String {
    return file.name
  }

  fun getFileUri(context: Context): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      FileProvider.getUriForFile(context, context.applicationContext.packageName + ".fileprovider", file)
    } else {
      Uri.fromFile(file)
    }
  }

  fun getSize(): String {
    val fileSizeKB: Int = round(file.length() / 1024.0).toInt()
    val fileSizeMB: Double = round(file.length() / 1048576.0 * 100.0) / 100.0
    return if (fileSizeMB < 1) fileSizeKB.toString() + " KB" else fileSizeMB.toString() + " MB"
  }

  fun getLastModified(context: Context): String {
    return DateFormat.getDateFormat(context).format(Date(file.lastModified())) + " " + DateFormat.getTimeFormat(context).format(
      Date(file.lastModified())
    )
  }

  fun getIcon(context: Context): Drawable {
    return packageInfo.applicationInfo.loadIcon(context.packageManager)
  }

  fun getAppName(context: Context): String {
    return context.packageManager.getApplicationLabel(packageInfo.applicationInfo).toString()
  }

  fun getAppPackage(): String {
    return packageInfo.packageName
  }

  fun getVersionName(): String {
    return packageInfo.versionName
  }

  fun getVersionCode(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) packageInfo.longVersionCode.toString() else packageInfo.versionCode.toString()
  }

  override fun compareTo(other: ApkFile): Int {
    return when {
      file.lastModified() > other.file.lastModified() -> -1
      file.lastModified() < other.file.lastModified() -> 1
      else -> 0
    }
  }

}