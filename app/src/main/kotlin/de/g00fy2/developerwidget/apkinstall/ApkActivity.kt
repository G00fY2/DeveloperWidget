package de.g00fy2.developerwidget.apkinstall

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import de.g00fy2.developerwidget.R
import kotlinx.android.synthetic.main.activity_apk.cancel_textview
import kotlinx.android.synthetic.main.activity_apk.install_textview
import kotlinx.android.synthetic.main.activity_apk.recyclerview
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.launch
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

    findAPKs(Environment.getExternalStorageDirectory())
  }

  private fun findAPKs(dir: File) {
    GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT, null, {
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
              adapter.add(apkFile)
            }
          }
        }
      }
    })
  }


  private fun installAPK() {
    val apk: ApkFile? = adapter.getSelectedFile()
    if (apk != null) {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.setDataAndType(Uri.fromFile(apk.file),
          APK_MIME_TYPE)
      intent.flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Intent.FLAG_GRANT_READ_URI_PERMISSION else Intent.FLAG_ACTIVITY_NEW_TASK
      startActivity(intent)
    }
  }

  override fun fileSelected() {
    install_textview.isEnabled = true
  }

  companion object {
    const val EXTENSION = ".apk"
    const val APK_MIME_TYPE = "application/vnd.android.package-archive"
  }
}