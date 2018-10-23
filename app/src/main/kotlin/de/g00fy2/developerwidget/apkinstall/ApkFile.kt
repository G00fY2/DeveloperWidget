package de.g00fy2.developerwidget.apkinstall

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Build
import android.text.format.DateFormat
import androidx.core.content.FileProvider
import androidx.core.content.pm.PackageInfoCompat
import java.io.File
import java.text.NumberFormat
import java.util.Date
import kotlin.math.round

class ApkFile(file: File, context: Context) : Comparable<ApkFile> {

  private val lastModifiedTimestamp = file.lastModified()
  private var packageInfo: PackageInfo? = context.packageManager.getPackageArchiveInfo(file.absolutePath, 0)

  val filename: String = file.name
  val lastModified = DateFormat.getDateFormat(context).let { dateFormat ->
    dateFormat.format(Date(lastModifiedTimestamp)) + " " + dateFormat.format(
      Date(lastModifiedTimestamp)
    )
  }
  val size = run {
    val fileSizeKB: Int = round(file.length() / 1024.0).toInt()
    val fileSizeMB: Double = round(file.length() / 1048576.0 * 100.0) / 100.0
    if (fileSizeMB < 1) fileSizeKB.toString() + " KB" else NumberFormat.getInstance().format(fileSizeMB) + " MB"
  }

  init {
    packageInfo?.applicationInfo?.sourceDir = file.absolutePath
    packageInfo?.applicationInfo?.publicSourceDir = file.absolutePath

  }

  val debuggable: Boolean =
    packageInfo?.applicationInfo?.flags?.let { it and (ApplicationInfo.FLAG_DEBUGGABLE) != 0 } ?: false
  val appName: CharSequence =
    packageInfo?.applicationInfo?.let { appInfo -> context.packageManager.getApplicationLabel(appInfo) } ?: ""
  val versionName = packageInfo?.versionName ?: ""
  val versionCode = packageInfo?.let { packInfo -> PackageInfoCompat.getLongVersionCode(packInfo).toString() } ?: 0L
  val valid = { packageInfo?.applicationInfo != null }

  val icon = {
    packageInfo?.applicationInfo?.let { applicationInfo -> context.packageManager.getApplicationIcon(applicationInfo) }
  }
  val fileUri = {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      FileProvider.getUriForFile(context, context.applicationContext.packageName + ".fileprovider", file)
    } else {
      Uri.fromFile(file)
    }
  }

  override fun compareTo(other: ApkFile): Int = when {
    lastModifiedTimestamp > other.lastModifiedTimestamp -> -1
    lastModifiedTimestamp < other.lastModifiedTimestamp -> 1
    else -> 0
  }
}