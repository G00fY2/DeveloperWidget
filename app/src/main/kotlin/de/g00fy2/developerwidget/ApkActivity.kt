package de.g00fy2.developerwidget

import android.app.Activity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_apk.cancel_textview
import kotlinx.android.synthetic.main.activity_apk.install_textview
import kotlinx.android.synthetic.main.activity_apk.recyclerview
import java.io.File


class ApkActivity : Activity(), OnSelectFileListener {

  private var apkFiles: MutableList<File> = ArrayList()
  private lateinit var adapter: ApkAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
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
    val listFile = dir.listFiles()

    if (listFile != null) {
      for (i in listFile.indices) {

        if (listFile[i].isDirectory) {
          findAPKs(listFile[i])
        } else {
          if (listFile[i].name.endsWith(EXTENSION, true)) {
            Log.d("APK found", (listFile[i].name))
            apkFiles.add((listFile[i]))
            adapter.add((listFile[i]))
          }
        }
      }
    }
  }

  private fun installAPK() {
    val apk: File? = adapter.getSelectedFile()
    if (apk != null) {
      Log.d("Install APK", apk.name)
    }
  }

  override fun fileSelected() {
    install_textview.isEnabled = true
  }

  companion object {
    const val EXTENSION = ".apk"
  }
}