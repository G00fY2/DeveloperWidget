package com.g00fy2.developerwidget.activities.apkinstall

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.text.format.DateFormat
import androidx.core.content.FileProvider
import androidx.core.content.pm.PackageInfoCompat
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.di.annotations.ACTIVITY
import timber.log.Timber
import java.io.File
import java.text.NumberFormat
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.round

class ApkFile private constructor() : Comparable<ApkFile> {

  var lastModifiedTimestamp: Long = 0
    private set
  var fileName: String = ""
    private set
  var lastModified: String = ""
    private set
  var size: String = ""
    private set
  var valid: Boolean = false
    private set
  var appName: String = ""
    private set
  var versionName: String = ""
    private set
  var versionCode: String = ""
    private set
  var debuggable: Boolean = false
    private set
  var appIcon: Drawable? = null
    private set
  var filePath = ""
    private set
  var fileUri: Uri? = null
    private set
  var dangerousPermissions: List<Pair<String, String?>> = emptyList()
    private set
  var targetSdkVersion: Int = 0
    private set

  override fun compareTo(other: ApkFile) = compareValues(other.lastModifiedTimestamp, lastModifiedTimestamp)

  interface ApkFileBuilder {

    fun build(file: File): ApkFile
  }

  class ApkFileBuilderImpl @Inject constructor(@Named(ACTIVITY) private val activity: BaseActivity) : ApkFileBuilder {

    private val dateFormat = DateFormat.getDateFormat(activity)
    private val timeFormat = DateFormat.getTimeFormat(activity)
    private val packageManager = activity.packageManager

    override fun build(file: File): ApkFile {
      return ApkFile().apply {
        lastModifiedTimestamp = file.lastModified()
        fileName = file.name
        lastModified = dateFormat.format(lastModifiedTimestamp) + " " + timeFormat.format(lastModifiedTimestamp)
        size = getFormattedSize(file.length())

        file.absolutePath.let { filePath ->
          if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            packageManager.getPackageArchiveInfo(
              filePath,
              PackageManager.PackageInfoFlags.of(PackageManager.GET_PERMISSIONS.toLong())
            )
          } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageArchiveInfo(filePath, PackageManager.GET_PERMISSIONS)
          }?.let { packageInfo ->
            packageInfo.versionName?.let { versionName = it }
            PackageInfoCompat.getLongVersionCode(packageInfo).let { versionCode = it.toString() }
            dangerousPermissions = extractDangerousPermissions(packageInfo.requestedPermissions)
            packageInfo.applicationInfo
          }?.let { appInfo ->
            valid = true
            targetSdkVersion = appInfo.targetSdkVersion
            appInfo.sourceDir = filePath
            appInfo.publicSourceDir = filePath
            packageManager.getApplicationLabel(appInfo).let { appName = it.toString() }
            debuggable = appInfo.flags.and(ApplicationInfo.FLAG_DEBUGGABLE) != 0
            packageManager.getApplicationIcon(appInfo).let {
              appIcon = if (VERSION.SDK_INT >= VERSION_CODES.O && it is AdaptiveIconDrawable) {
                InsetDrawable(it, 0.025f, 0.01f, 0.025f, 0.04f)
              } else {
                it
              }
            }
          }
        }

        filePath = file.path
        try {
          fileUri = if (VERSION.SDK_INT >= VERSION_CODES.N) {
            FileProvider.getUriForFile(activity, activity.applicationContext.packageName + ".fileprovider", file)
          } else {
            Uri.fromFile(file)
          }
        } catch (e: IllegalArgumentException) {
          Timber.e(e)
        }
      }
    }

    private fun extractDangerousPermissions(requestedPermissions: Array<String>?): List<Pair<String, String?>> {
      return mutableListOf<Pair<String, String?>>().apply {
        requestedPermissions?.distinct()?.forEach { permission ->
          try {
            packageManager.getPermissionInfo(permission, 0).let {
              if (it.hasDangerousPermissions()) {
                add(Pair(it.name.substringAfterLast("."), it.loadDescription(packageManager)?.toString()))
              }
            }
          } catch (e: Exception) {
            // unknown permission qualifier
          }
        }
      }
    }

    private fun PermissionInfo.hasDangerousPermissions(): Boolean {
      return if (VERSION.SDK_INT >= VERSION_CODES.P) {
        protection == PermissionInfo.PROTECTION_DANGEROUS
      } else {
        @Suppress("DEPRECATION")
        protectionLevel == PermissionInfo.PROTECTION_DANGEROUS
      }
    }

    private fun getFormattedSize(bytes: Long): String {
      return (round(bytes / 1048576.0 * 100.0) / 100.0).let { sizeMB ->
        if (sizeMB < 1) {
          round(bytes / 1024.0).toInt().toString() + " KB"
        } else {
          NumberFormat.getInstance().format(sizeMB) + " MB"
        }
      }
    }
  }
}