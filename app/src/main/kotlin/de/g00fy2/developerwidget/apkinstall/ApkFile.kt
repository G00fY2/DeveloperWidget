package de.g00fy2.developerwidget.apkinstall

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.format.DateFormat
import androidx.core.content.FileProvider
import androidx.core.content.pm.PackageInfoCompat
import java.io.File
import java.text.NumberFormat
import java.util.Date
import kotlin.math.round

class ApkFile(private var file: File, context: Context) : Comparable<ApkFile> {

  private lateinit var packageInfo: PackageInfo
  private var validApk: Boolean = true

  init {
    try {
      packageInfo = context.packageManager.getPackageArchiveInfo(file.absolutePath, 0)
      packageInfo.applicationInfo.sourceDir = file.absolutePath
      packageInfo.applicationInfo.publicSourceDir = file.absolutePath
    } catch (e: IllegalStateException) {
      validApk = false
    }
  }

  fun isValid() = validApk

  fun getFileName(): String = file.name

  fun getFilePath(): String {
    val path = file.parent.substring(Environment.getExternalStorageDirectory().absolutePath.length)
    return if (path.isEmpty()) "/" else path
  }

  fun getFileUri(context: Context): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      FileProvider.getUriForFile(context, context.applicationContext.packageName + ".fileprovider", file)
    } else {
      Uri.fromFile(file)
    }
  }

  fun isDebuggable() = packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

  fun getSize(): String {
    val fileSizeKB: Int = round(file.length() / 1024.0).toInt()
    val fileSizeMB: Double = round(file.length() / 1048576.0 * 100.0) / 100.0
    return if (fileSizeMB < 1) fileSizeKB.toString() + " KB" else NumberFormat.getInstance().format(fileSizeMB) + " MB"
  }

  fun getLastModified(context: Context): String {
    return DateFormat.getDateFormat(context).format(Date(file.lastModified())) + " " + DateFormat.getTimeFormat(context).format(
      Date(file.lastModified())
    )
  }

  fun getIcon(context: Context): Drawable = packageInfo.applicationInfo.loadIcon(context.packageManager)

  fun getAppName(context: Context) = context.packageManager.getApplicationLabel(packageInfo.applicationInfo).toString()

  fun getAppPackage(): String = packageInfo.packageName

  fun getVersionName(): String = packageInfo.versionName

  fun getVersionCode() = PackageInfoCompat.getLongVersionCode(packageInfo).toString()

  override fun compareTo(other: ApkFile): Int = when {
    file.lastModified() > other.file.lastModified() -> -1
    file.lastModified() < other.file.lastModified() -> 1
    else -> 0
  }
}