package de.g00fy2.developerwidget.apkinstall

import android.Manifest
import android.Manifest.permission
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.R.string
import kotlinx.android.synthetic.main.activity_apk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import kotlin.coroutines.CoroutineContext

class ApkActivity : Activity(), CoroutineScope {

  private var apkFiles: MutableList<ApkFile> = ArrayList()
  private var rootPathLength = 0
  private lateinit var adapter: ApkAdapter
  private lateinit var job: Job

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    job = Job()
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    setContentView(R.layout.activity_apk)

    val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
    val height = (resources.displayMetrics.heightPixels * 0.80).toInt()
    window.setLayout(width, height)

    cancel_textview.setOnClickListener { finish() }
    install_textview.setOnClickListener { installAPK() }

    adapter = ApkAdapter()
    adapter.setOnApkSelectedListener { install_textview.isEnabled = true }
    recyclerview.layoutManager = LinearLayoutManager(this)
    recyclerview.adapter = adapter

    requestPermissions()
  }

  override fun onResume() {
    super.onResume()
    initState()
    if (hasPermissions()) {
      launch {
        Environment.getExternalStorageDirectory().let {
          rootPathLength = it.absolutePath.length
          findAPKs(it)
        }
      }.invokeOnCompletion {
        Timber.d("parent job finished")
        toggleResultView()
      }
    } else {
      toggleResultView(true)
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    job.cancel()
  }

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + job

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

  private suspend fun findAPKs(dir: File) {
    launch {
      result_textview.text = dir.path.substring(rootPathLength)
      dir.listFiles()?.let {
        for (i in it.indices) {
          if (it[i].isDirectory) {
            findAPKs(it[i])
          } else {
            if (it[i].name.endsWith(APK_EXTENSION, true)) {
              Timber.d("APK found %s", (it[i].name))
              ApkFile(it[i], this@ApkActivity).let { apkFile ->
                if (apkFile.valid) apkFiles.add(apkFile)
              }
            }
          }
        }
      }
    }.apply {
      join()
      invokeOnCompletion {
        Timber.d("scanned folder $dir")
      }
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
    result_textview.visibility = View.VISIBLE
    recyclerview.overScrollMode = View.OVER_SCROLL_NEVER
    apkFiles.clear()
    adapter.clear()
  }

  private fun toggleResultView(missingPermissions: Boolean = false) {
    if (missingPermissions) {
      progressbar.visibility = View.INVISIBLE
      result_textview.text = getString(string.missing_permissions)
      return
    }

    if (apkFiles.size > 0) {
      adapter.addAll(apkFiles)
      recyclerview.overScrollMode = View.OVER_SCROLL_ALWAYS
      result_textview.visibility = View.INVISIBLE
    } else {
      result_textview.text = getString(string.no_apk_found)
    }

    ViewCompat.animate(progressbar).alpha(0f).setDuration(400).withEndAction {
      progressbar.visibility = View.INVISIBLE
    }.start()
  }

  companion object {
    const val APK_EXTENSION = ".apk"
    const val APK_MIME_TYPE = "application/vnd.android.package-archive"
  }
}