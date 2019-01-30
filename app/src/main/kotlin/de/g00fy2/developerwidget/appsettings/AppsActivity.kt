package de.g00fy2.developerwidget.appsettings

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.base.BaseActivity
import kotlinx.android.synthetic.main.activity_apps.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppsActivity : BaseActivity() {

  override val layoutRes = R.layout.activity_apps
  private lateinit var appInfoBuilder: AppInfo.Builder
  private lateinit var adapter: AppsAdapter
  private var installedAppPackages: MutableList<AppInfo> = ArrayList()

  override fun onCreate(savedInstanceState: Bundle?) {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    super.onCreate(savedInstanceState)

    val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
    val height = (resources.displayMetrics.heightPixels * 0.80).toInt()
    window.setLayout(width, height)

    cancel_textview.setOnClickListener { finish() }

    adapter = AppsAdapter().setOnAppSelected { openAppSettingsActivity() }
    recyclerview.setHasFixedSize(true)
    recyclerview.layoutManager = LinearLayoutManager(this)
    recyclerview.adapter = adapter
    appInfoBuilder = AppInfo.Builder(this)
    initFilterViews()
  }

  override fun onResume() {
    super.onResume()
    launch {
      getInstalledUserApps()
      toggleResultView()
    }
  }

  private fun initFilterViews() {
    fiter_imageview.apply {
      setOnClickListener {
        if (filter_linearlayout.visibility == View.VISIBLE) {
          filter_linearlayout.visibility = View.GONE
          adapter.resetAppFilter()
          filter_edittext.text.clear()
          hideKeyboard()
        } else {
          filter_linearlayout.visibility = View.VISIBLE
        }
      }
    }

    filter_edittext.apply {
      filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
        if (source.isEmpty() || source.matches("^[a-zA-Z0-9._*]*$".toRegex())) {
          null
        } else {
          ""
        }
      })
      setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE && filter_edittext.text.trim().isNotEmpty()) {
          // TODO save filter
          (LayoutInflater.from(context).inflate(R.layout.filter_chip, flexbox_layout, false) as Chip).let {
            it.text = filter_edittext.text.trim()
            flexbox_layout.addView(it)
          }
          filter_edittext.text.clear()
        }
        true
      }
      addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
          adapter.updateAppFilter(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
      })
    }
  }

  private fun openAppSettingsActivity() {
    adapter.getSelectedPackageName()?.let {
      startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$it")))
    }
  }

  private fun addFilterChips(filters: Set<String>) {
    for (i in filters) {
      flexbox_layout.addView(Chip(this).apply {
        text = i
        isCloseIconVisible = true
        setOnCloseIconClickListener { filters.minus(i) }
      })
    }
  }

  private suspend fun getInstalledUserApps() {
    installedAppPackages = withContext(Dispatchers.IO) {
      packageManager.getInstalledPackages(0)
        .filter { it.applicationInfo.flags and (ApplicationInfo.FLAG_SYSTEM or ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0 }
        .map { appInfoBuilder.build(it) }
        .sorted()
        .toMutableList()
    }
  }

  private fun toggleResultView() {
    if (installedAppPackages.isNotEmpty()) {
      adapter.addAll(installedAppPackages.filter { it.packageName.contains("", true) }.toMutableList())
      recyclerview.overScrollMode = View.OVER_SCROLL_ALWAYS
      progress_textview.visibility = View.INVISIBLE
    } else {
      progress_textview.text = getString(R.string.no_apk_found)
    }

    ViewCompat.animate(progressbar).alpha(0f).setDuration(400).withEndAction {
      progressbar.visibility = View.INVISIBLE
    }.start()
  }
}