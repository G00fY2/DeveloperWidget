package com.g00fy2.developerwidget.activities.apkinstall

import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.format.DateFormat
import androidx.core.content.FileProvider
import androidx.core.content.pm.PackageInfoCompat
import com.g00fy2.developerwidget.utils.ACTIVITY
import timber.log.Timber
import java.io.File
import java.text.NumberFormat
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.round

class ApkFile private constructor() : Comparable<ApkFile> {

  var lastModifiedTimestamp: Long = 0; private set
  var fileName: String = ""; private set
  var lastModified: String = ""; private set
  var size: String = ""; private set
  var valid: Boolean = false; private set
  var appName: String = ""; private set
  var versionName: String = ""; private set
  var versionCode: String = ""; private set
  var debuggable: Boolean = false; private set
  var appIcon: Drawable? = null; private set
  var filePath = ""; private set
  var fileUri: Uri? = null; private set

  override fun compareTo(other: ApkFile) = compareValues(other.lastModifiedTimestamp, lastModifiedTimestamp)

  // TODO split builder from data object
  interface ApkFileBuilder {

    fun build(file: File): ApkFile
  }

  class ApkFileBuilderImpl @Inject constructor(@Named(ACTIVITY) private val context: Context) : ApkFileBuilder {

    private val dateFormat = DateFormat.getDateFormat(context)
    private val timeFormat = DateFormat.getTimeFormat(context)
    private val packageManager = context.packageManager

    override fun build(file: File): ApkFile {
      return ApkFile().apply {
        lastModifiedTimestamp = file.lastModified()
        fileName = file.name
        lastModified = dateFormat.format(lastModifiedTimestamp) + " " + timeFormat.format(lastModifiedTimestamp)
        size = {
          file.length().let { bytes ->
            (round(bytes / 1048576.0 * 100.0) / 100.0).let { sizeMB ->
              if (sizeMB < 1) {
                round(bytes / 1024.0).toInt().toString() + " KB"
              } else {
                NumberFormat.getInstance().format(sizeMB) + " MB"
              }
            }
          }
        }()

        file.absolutePath.let { filePath ->
          packageManager.getPackageArchiveInfo(filePath, 0)?.let { packageInfo ->
            versionName = packageInfo.versionName
            versionCode = PackageInfoCompat.getLongVersionCode(packageInfo).toString()
            packageInfo.applicationInfo
          }?.let { appInfo ->
            valid = true
            appInfo.sourceDir = filePath
            appInfo.publicSourceDir = filePath
            appName = packageManager.getApplicationLabel(appInfo).toString()
            appIcon = packageManager.getApplicationIcon(appInfo)
            debuggable = appInfo.flags.and(ApplicationInfo.FLAG_DEBUGGABLE) != 0
          }
        }

        filePath = file.path
        try {
          fileUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context, context.applicationContext.packageName + ".fileprovider", file)
          } else {
            Uri.fromFile(file)
          }
        } catch (e: IllegalArgumentException) {
          Timber.e(e)
        }
      }
    }
  }
}