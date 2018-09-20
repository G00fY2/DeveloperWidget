package de.g00fy2.developerwidget.apkinstall

import android.Manifest
import android.Manifest.permission
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.R.string
import kotlinx.android.synthetic.main.activity_apk.cancel_textview
import kotlinx.android.synthetic.main.activity_apk.empty_recyclerview_textview
import kotlinx.android.synthetic.main.activity_apk.install_textview
import kotlinx.android.synthetic.main.activity_apk.progressbar
import kotlinx.android.synthetic.main.activity_apk.recyclerview
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.launch
import java.io.File
import kotlin.coroutines.experimental.CoroutineContext


class ApkActivity : Activity(), CoroutineScope, OnSelectFileListener {

  private lateinit var pm: PackageManager
  private var apkFiles: MutableList<ApkFile> = ArrayList()
  private lateinit var adapter: ApkAdapter
  private lateinit var job: Job

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    job = Job()
    pm = packageManager
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    setContentView(R.layout.activity_apk)

    val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
    val height = (resources.displayMetrics.heightPixels * 0.80).toInt()
    window.setLayout(width, height)

    cancel_textview.setOnClickListener { finish() }
    install_textview.setOnClickListener { installAPK() }


    adapter = ApkAdapter(this)
    recyclerview.setHasFixedSize(true)
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
        adapter.addAll(apkFiles)
        toggleResultView(false)
        Log.d("coroutines", "parent job finished")
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
    if (!hasPermissions()) {
      requestPermissions(arrayOf(permission.READ_EXTERNAL_STORAGE), 1)
    }
  }

  private fun hasPermissions(): Boolean {
    return if (VERSION.SDK_INT >= VERSION_CODES.M) {
      ContextCompat.checkSelfPermission(this,
          Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
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
            if (listFile[i].name.endsWith(EXTENSION, true)) {
              Log.d("APK found", (listFile[i].name))
              val apkFile = ApkFile(listFile[i], pm)
              apkFiles.add(apkFile)
            }
          }
        }
      }
    }
    scanJob.join()
    scanJob.invokeOnCompletion {
      Log.d("coroutines", "child job finished")
    }
  }

  private fun installAPK() {
    val apk: ApkFile? = adapter.getSelectedFile()
    if (apk != null) {
      val intent = Intent(Intent.ACTION_VIEW)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.setDataAndType(getFileUri(apk.file), APK_MIME_TYPE)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
      } else {
        intent.setDataAndType(Uri.fromFile(apk.file), APK_MIME_TYPE)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
      }
      startActivity(intent)
    }
  }

  private fun initState() {
    install_textview.isEnabled = false
    progressbar.visibility = View.VISIBLE
    recyclerview.visibility = View.GONE
    empty_recyclerview_textview.visibility = View.GONE
    apkFiles.clear()
    adapter.clear()
  }

  private fun toggleResultView(missingPermissions: Boolean) {
    ViewCompat.animate(progressbar).alpha(0f).setDuration(200).withEndAction {
      progressbar.visibility = View.GONE
      progressbar.alpha = 1f
    }.start()

    if (apkFiles.size > 0) {
      empty_recyclerview_textview.visibility = View.GONE
      recyclerview.visibility = View.VISIBLE
    } else {
      if (missingPermissions) {
        empty_recyclerview_textview.text = getString(string.missing_permissions)
      } else {
        empty_recyclerview_textview.text = getString(string.no_apk_found)
      }
      empty_recyclerview_textview.visibility = View.VISIBLE
      recyclerview.visibility = View.GONE
    }
  }

  private fun getFileUri(file: File): Uri {
    return FileProvider.getUriForFile(this, applicationContext.packageName + ".fileprovider", file)
  }

  override fun fileSelected() {
    install_textview.isEnabled = true
  }

  companion object {
    const val EXTENSION = ".apk"
    const val APK_MIME_TYPE = "application/vnd.android.package-archive"
  }
}