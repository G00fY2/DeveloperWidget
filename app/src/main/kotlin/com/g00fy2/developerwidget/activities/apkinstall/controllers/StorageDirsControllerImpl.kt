package com.g00fy2.developerwidget.activities.apkinstall.controllers

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Environment
import androidx.annotation.RequiresApi
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.di.annotations.ACTIVITY
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
  @Named(ACTIVITY)
  lateinit var activity: BaseActivity

  // return all storage directories (inspired by AOSP API 18 and the Amaze File Manager sourcecode)
  override fun getStorageDirectories(): Collection<File> {
    return mutableSetOf<File>().apply {
      if (VERSION.SDK_INT < VERSION_CODES.M) {
        addAll(getExtSdCardPathsDeprecated())
      }
      if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
        addAll(getExtSdCardPaths())
      }
    }
  }

  @Suppress("DEPRECATION")
  // TODO getExternalStorageDirectory and requestLegacyExternalStorage only work with targetSdk <= 29
  //  If we want to raise the targetSdk we would have to switch to Androids Scoped storage API, which doesn not
  //  offer access to all folders
  private fun getExtSdCardPathsDeprecated(): Collection<File> {
    val dirs = mutableListOf<File>()
    val rawExternalStorage = System.getenv("EXTERNAL_STORAGE")
    val rawSecondaryStorages = System.getenv("SECONDARY_STORAGE")
    val rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET")

    if (rawEmulatedStorageTarget.isNullOrEmpty()) {
      if (rawExternalStorage.isNullOrEmpty()) {
        File("/storage/sdcard0").let {
          if (it.exists()) dirs.add(it) else dirs.add(Environment.getExternalStorageDirectory())
        }
      } else {
        dirs.add(File(rawExternalStorage))
      }
    } else {
      val rawUserId = if (VERSION.SDK_INT < VERSION_CODES.JELLY_BEAN_MR1) {
        ""
      } else {
        val path = Environment.getExternalStorageDirectory().absolutePath
        val folders = Pattern.compile("/").split(path)
        val lastFolder = folders[folders.size - 1]

        if (lastFolder.toIntOrNull() != null) lastFolder else ""
      }
      if (rawUserId.isEmpty()) {
        dirs.add(File(rawEmulatedStorageTarget))
      } else {
        dirs.add(File(rawEmulatedStorageTarget + separator + rawUserId))
      }
    }
    if (!rawSecondaryStorages.isNullOrEmpty()) {
      rawSecondaryStorages.split(pathSeparator.toRegex()).dropLastWhile { it.isEmpty() }.map { File(it) }.let {
        dirs.addAll(it)
      }
    }
    return dirs
  }

  @RequiresApi(VERSION_CODES.KITKAT)
  private fun getExtSdCardPaths(): Collection<File> {
    val dirs = mutableListOf<File>()
    for (file in activity.getExternalFilesDirs("external")) {
      file?.let {
        val index = it.absolutePath.lastIndexOf("/Android/data")
        if (index < 0) {
          Timber.w("Unexpected external file dir: %s", it.absolutePath)
        } else {
          var path = it.absolutePath.substring(0, index)
          try {
            path = File(path).canonicalPath
          } catch (e: IOException) {
            // Keep non-canonical path
          }
          dirs.add(File(path))
        }
      }
    }
    if (dirs.isEmpty()) {
      dirs.add(File("/storage/sdcard1"))
    }
    return dirs.filter { it.canRead() && it.isDirectory }
  }
}