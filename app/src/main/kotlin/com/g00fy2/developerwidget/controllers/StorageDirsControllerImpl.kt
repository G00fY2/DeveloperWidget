package com.g00fy2.developerwidget.controllers

import android.annotation.TargetApi
import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Environment
import com.g00fy2.developerwidget.utils.ACTIVITY
import timber.log.Timber
import java.io.File
import java.io.File.pathSeparator
import java.io.File.separator
import java.io.IOException
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Named


class StorageDirsControllerImpl @Inject constructor() : StorageDirsController {

  @Inject
  @field:Named(ACTIVITY)
  lateinit var context: Context

  override fun getStorageDirectories(): List<String> {
    val rv = mutableListOf<String>()
    val rawExternalStorage = System.getenv("EXTERNAL_STORAGE")
    val rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE")
    val rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET")

    if (VERSION.SDK_INT < VERSION_CODES.M) {
      if (rawEmulatedStorageTarget.isNullOrEmpty()) {
        if (rawExternalStorage.isNullOrEmpty()) {
          if (File(DEFAULT_FALLBACK_STORAGE_PATH).exists()) {
            rv.add(DEFAULT_FALLBACK_STORAGE_PATH)
          } else {
            rv.add(Environment.getExternalStorageDirectory().absolutePath)
          }
        } else {
          rv.add(rawExternalStorage)
        }
      } else {
        val rawUserId = if (VERSION.SDK_INT < VERSION_CODES.JELLY_BEAN_MR1) {
          ""
        } else {
          val path = Environment.getExternalStorageDirectory().absolutePath
          val folders = DIR_SEPARATOR.split(path)
          val lastFolder = folders[folders.size - 1]

          if (lastFolder.toIntOrNull() != null) lastFolder else ""
        }
        if (rawUserId.isEmpty()) {
          rv.add(rawEmulatedStorageTarget)
        } else {
          rv.add(rawEmulatedStorageTarget + separator + rawUserId)
        }
      }
      if (!rawSecondaryStoragesStr.isNullOrEmpty()) {
        val rawSecondaryStorages =
          rawSecondaryStoragesStr.split(pathSeparator.toRegex()).dropLastWhile { it.isEmpty() }
        rv.addAll(rawSecondaryStorages)
      }
    }

    if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
      getExtSdCardPaths().let {
        for (s in it) {
          val f = File(s)
          if (!rv.contains(s) && f.canRead() && f.isDirectory) {
            rv.add(s)
          }
        }
      }
    }

    return rv
  }

  @TargetApi(VERSION_CODES.KITKAT)
  private fun getExtSdCardPaths(): List<String> {
    val paths = mutableListOf<String>()
    for (file in context.getExternalFilesDirs("external")) {
      file?.let {
        val index = it.absolutePath.lastIndexOf("/Android/data")
        if (index < 0) {
          Timber.w("Unexpected external file dir: %s", it.absolutePath)
        } else {
          var path = it.absolutePath.substring(0, index)
          try {
            path = File(path).canonicalPath
          } catch (e: IOException) {
            // Keep non-canonical path.
          }
          paths.add(path)
        }
      }
    }
    if (paths.isEmpty()) paths.add("/storage/sdcard1")
    return paths
  }

  companion object {
    private const val DEFAULT_FALLBACK_STORAGE_PATH = "/storage/sdcard0"
    private val DIR_SEPARATOR: Pattern = Pattern.compile("/")
  }
}