package de.g00fy2.developerwidget.apkinstall

import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
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
  var fileName = ""; private set
  var lastModified = ""; private set
  var size = ""; private set
  var valid = false; private set
  var appName = ""; private set
  var versionName = ""; private set
  var versionCode = ""; private set
  var debuggable = false; private set
  var appIcon: Drawable? = null; private set
  var fileUri: Uri? = null; private set

  init {
    fileName = file.name
    lastModified = DateFormat.getDateFormat(context).let { dateFormat ->
      dateFormat.format(Date(lastModifiedTimestamp)) + " " + dateFormat.format(
        Date(lastModifiedTimestamp)
      )
    }
    size = run {
      val fileSizeKB: Int = round(file.length() / 1024.0).toInt()
      val fileSizeMB: Double = round(file.length() / 1048576.0 * 100.0) / 100.0
      if (fileSizeMB < 1) fileSizeKB.toString() + " KB" else NumberFormat.getInstance().format(fileSizeMB) + " MB"
    }

    context.packageManager.getPackageArchiveInfo(file.absolutePath, 0)?.let { packageInfo ->
      versionName = packageInfo.versionName
      versionCode = PackageInfoCompat.getLongVersionCode(packageInfo).toString()
      packageInfo.applicationInfo?.let { applicationInfo ->
        valid = true
        file.absolutePath.let { path ->
          applicationInfo.sourceDir = path
          applicationInfo.publicSourceDir = path
        }
        appName = context.packageManager.getApplicationLabel(applicationInfo).toString()
        appIcon = context.packageManager.getApplicationIcon(applicationInfo)
        debuggable = applicationInfo.flags.and(ApplicationInfo.FLAG_DEBUGGABLE) != 0
      }
    }

    fileUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
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