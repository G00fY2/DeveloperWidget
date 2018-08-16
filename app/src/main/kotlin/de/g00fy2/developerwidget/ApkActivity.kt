package de.g00fy2.developerwidget

import android.app.Activity
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_apk.cancel_textview
import kotlinx.android.synthetic.main.activity_apk.install_textview
import kotlinx.android.synthetic.main.activity_apk.recyclerview
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File


class ApkActivity : Activity(), OnSelectFileListener {

  private lateinit var pm: PackageManager
  private var apkFiles: MutableList<ApkFile> = ArrayList()
  private lateinit var adapter: ApkAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
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
  }

  override fun onResume() {
    super.onResume()
    adapter.clear()
    findAPKs(Environment.getExternalStorageDirectory())
  }

  private fun findAPKs(dir: File) {
    doAsync {
      val listFile = dir.listFiles()

      if (listFile != null) {
        for (i in listFile.indices) {
          if (listFile[i].isDirectory) {
            findAPKs(listFile[i])
          } else {
            if (listFile[i].name.endsWith(EXTENSION, true)) {
              uiThread {
                Log.d("APK found", (listFile[i].name))
                val apkFile = fileToApkFile(listFile[i])
                apkFiles.add(apkFile)
                adapter.add(apkFile)
              }
            }
          }
        }
      }

    }
  }

  private fun fileToApkFile(file: File): ApkFile {
    val packageInfo: PackageInfo = pm.getPackageArchiveInfo(file.absolutePath, 0)
    val pi = packageInfo.applicationInfo
    val apkFile = ApkFile()
    pi.sourceDir = file.absolutePath
    pi.publicSourceDir = file.absolutePath
    apkFile.fileName = file.nameWithoutExtension
    apkFile.filePath = file.absolutePath
    apkFile.apkIcon = pi.loadIcon(pm)
    return apkFile
  }

  private fun installAPK() {
    val apk: ApkFile? = adapter.getSelectedFile()
    if (apk != null) {
      Log.d("Install APK", apk.fileName)
    }
  }

  override fun fileSelected() {
    install_textview.isEnabled = true
  }

  companion object {
    const val EXTENSION = ".apk"
  }
}