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
import kotlinx.android.synthetic.main.activity_apk.cancel_textview
import kotlinx.android.synthetic.main.activity_apk.install_textview
import kotlinx.android.synthetic.main.activity_apk.no_result_textview
import kotlinx.android.synthetic.main.activity_apk.progressbar
import kotlinx.android.synthetic.main.activity_apk.recyclerview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import kotlin.coroutines.CoroutineContext

class ApkActivity : Activity(), CoroutineScope {

  private var apkFiles: MutableList<ApkFile> = ArrayList()
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

    adapter = ApkAdapter(this)
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
        findAPKs(Environment.getExternalStorageDirectory())
      }.invokeOnCompletion {
        Timber.d("parent job finished")
        adapter.addAll(apkFiles)
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
    val scanJob = launch {
      val listFile = dir.listFiles()

      if (listFile != null) {
        for (i in listFile.indices) {
          if (listFile[i].isDirectory) {
            findAPKs(listFile[i])
          } else {
            if (listFile[i].name.endsWith(APK_EXTENSION, true)) {
              Timber.d("APK found %s", (listFile[i].name))
              val apkFile = ApkFile(listFile[i], this@ApkActivity)
              if (apkFile.isValid()) apkFiles.add(apkFile)
            }
          }
        }
      }
    }
    scanJob.join()
    scanJob.invokeOnCompletion {
      Timber.d("scanned folder $dir")
    }
  }

  private fun installAPK() {
    val apk: ApkFile? = adapter.getSelectedFile()
    if (apk != null) {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.setDataAndType(apk.getFileUri(this), APK_MIME_TYPE)
      intent.flags =
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Intent.FLAG_GRANT_READ_URI_PERMISSION else Intent.FLAG_ACTIVITY_NEW_TASK
      startActivity(intent)
    }
  }

  private fun initState() {
    install_textview.isEnabled = false
    progressbar.visibility = View.VISIBLE
    recyclerview.visibility = View.INVISIBLE
    no_result_textview.visibility = View.INVISIBLE
    apkFiles.clear()
    adapter.clear()
  }

  private fun toggleResultView(missingPermissions: Boolean = false) {
    if (missingPermissions) {
      progressbar.visibility = View.INVISIBLE
    } else {
      ViewCompat.animate(progressbar).alpha(0f).setDuration(200).withEndAction {
        progressbar.visibility = View.INVISIBLE
        progressbar.alpha = 1f
      }.start()
    }

    if (apkFiles.size > 0) {
      no_result_textview.visibility = View.INVISIBLE
      recyclerview.visibility = View.VISIBLE
    } else {
      if (missingPermissions) {
        no_result_textview.text = getString(string.missing_permissions)
      } else {
        no_result_textview.text = getString(string.no_apk_found)
      }
      recyclerview.visibility = View.INVISIBLE
      no_result_textview.visibility = View.VISIBLE
    }
  }

  companion object {
    const val APK_EXTENSION = ".apk"
    const val APK_MIME_TYPE = "application/vnd.android.package-archive"
  }
}