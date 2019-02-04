package de.g00fy2.developerwidget.apkinstall

import android.Manifest
import android.Manifest.permission
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.Window
import androidx.annotation.ContentView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.base.BaseActivity
import kotlinx.android.synthetic.main.activity_apk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@ContentView(R.layout.activity_apk)
class ApkActivity : BaseActivity() {

  private lateinit var apkFileBuilder: ApkFile.Builder
  private lateinit var adapter: ApkAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    super.onCreate(savedInstanceState)

    val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
    val height = (resources.displayMetrics.heightPixels * 0.80).toInt()
    window.setLayout(width, height)

    cancel_textview.setOnClickListener { finish() }
    install_textview.setOnClickListener { installAPK() }

    adapter = ApkAdapter().setOnApkSelected { install_textview.isEnabled = true }
    recyclerview.setHasFixedSize(true)
    recyclerview.layoutManager = LinearLayoutManager(this)
    recyclerview.adapter = adapter
    apkFileBuilder = ApkFile.Builder(this)
    requestPermissions()
  }

  override fun onResume() {
    super.onResume()
    initState()
    if (hasPermissions()) {
      launch {
        val apkFiles = searchAPKs(Environment.getExternalStorageDirectory())
        toggleResultView(apkFiles)
      }
    } else {
      toggleResultView(missingPermissions = true)
    }
  }

  @TargetApi(VERSION_CODES.M)
  private fun requestPermissions() {
    if (!hasPermissions()) requestPermissions(arrayOf(permission.READ_EXTERNAL_STORAGE), 1)
  }

  private fun hasPermissions(): Boolean {
    return if (VERSION.SDK_INT >= VERSION_CODES.M) {
      ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
      ) == PackageManager.PERMISSION_GRANTED
    } else {
      true
    }
  }

  private suspend fun searchAPKs(dir: File): MutableList<ApkFile> {
    return withContext(Dispatchers.IO) {
      dir.walk()
        .filter { !it.isDirectory }
        .filter { it.extension.equals(APK_EXTENSION, true) }
        .map { apkFileBuilder.build(it) }
        .filter { it.valid }
        .sorted()
        .toMutableList()
    }
  }

  private fun installAPK() {
    adapter.getSelectedFile()?.let { apkFile ->
      Intent(Intent.ACTION_VIEW).also { intent ->
        intent.setDataAndType(apkFile.fileUri, APK_MIME_TYPE)
        intent.flags =
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Intent.FLAG_GRANT_READ_URI_PERMISSION else Intent.FLAG_ACTIVITY_NEW_TASK
      }.let { intent -> startActivity(intent) }
    }
  }

  private fun initState() {
    install_textview.isEnabled = false
    progressbar.alpha = 1f
    progressbar.visibility = View.VISIBLE
    progress_textview.visibility = View.VISIBLE
    progress_textview.text = getString(R.string.scanning_apks)
    recyclerview.overScrollMode = View.OVER_SCROLL_NEVER
    adapter.clear()
  }

  private fun toggleResultView(apkFiles: MutableList<ApkFile> = ArrayList(), missingPermissions: Boolean = false) {
    if (missingPermissions) {
      progressbar.visibility = View.INVISIBLE
      progress_textview.text = getString(R.string.missing_permissions)
      return
    }

    if (apkFiles.isNotEmpty()) {
      adapter.setItems(apkFiles)
      recyclerview.overScrollMode = View.OVER_SCROLL_ALWAYS
      progress_textview.visibility = View.INVISIBLE
    } else {
      progress_textview.text = getString(R.string.no_apk_found)
    }

    ViewCompat.animate(progressbar).alpha(0f)
      .setDuration(resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()).withEndAction {
        progressbar.visibility = View.INVISIBLE
      }.start()
  }

  companion object {
    const val APK_EXTENSION = "apk"
    const val APK_MIME_TYPE = "application/vnd.android.package-archive"
  }
}