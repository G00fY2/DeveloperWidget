package de.g00fy2.developerwidget.appsettings

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import android.view.inputmethod.EditorInfo
import androidx.annotation.ContentView
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.base.BaseActivity
import de.g00fy2.developerwidget.util.AnimationUtils
import de.g00fy2.developerwidget.util.SharedPreferencesHelper
import de.g00fy2.developerwidget.util.UiUtils
import kotlinx.android.synthetic.main.activity_apps.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ContentView(R.layout.activity_apps)
class AppsActivity : BaseActivity() {

  private lateinit var appInfoBuilder: AppInfo.Builder
  private lateinit var adapter: AppsAdapter
  private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
  private var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID
  private var installedAppPackages = mutableListOf<AppInfo>()
  private var appFilter = mutableListOf<String>()

  override fun onCreate(savedInstanceState: Bundle?) {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    super.onCreate(savedInstanceState)

    intent.extras?.let {
      widgetId = it.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
    }

    val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
    val height = (resources.displayMetrics.heightPixels * 0.80).toInt()
    window.setLayout(width, height)

    cancel_textview.setOnClickListener { finish() }

    adapter = AppsAdapter().setOnAppSelected { openAppSettingsActivity() }
    recyclerview.setHasFixedSize(true)
    recyclerview.itemAnimator = null
    recyclerview.layoutManager = LinearLayoutManager(this)
    recyclerview.adapter = adapter
    appInfoBuilder = AppInfo.Builder(this)
    sharedPreferencesHelper = SharedPreferencesHelper(this, widgetId)
    appFilter = sharedPreferencesHelper.getFilters()
    initFilterViews()
  }

  override fun onResume() {
    super.onResume()
    updateFilterIcon()
    launch {
      getInstalledUserApps()
      toggleResultView()
    }
  }

  override fun onBackPressed() {
    if (filter_linearlayout.isVisible) {
      toggleFilterView()
    } else {
      super.onBackPressed()
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

  private fun initFilterViews() {
    fiter_imageview.setOnClickListener { toggleFilterView() }

    filter_edittext.apply {
      filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
        if (source.isEmpty() || source.matches("^[a-zA-Z0-9._*]*$".toRegex())) {
          null
        } else {
          ""
        }
      })
      setOnEditorActionListener { _, actionId, _ ->
        val filterString = filter_edittext.text.toString().trim()
        if (actionId == EditorInfo.IME_ACTION_DONE && filterString.isNotEmpty() && !appFilter.contains(filterString)) {
          addAppFilter(filterString)
        }
        true
      }
      addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
          if (s.isNullOrEmpty()) {
            adapter.updateAppFilters(appFilter)
          } else {
            adapter.updateAppFilter(s.toString())
          }
          showNoItemView(adapter.itemCount == 0)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
      })
    }
    setFilterChips(appFilter)
    filter_linearlayout.apply {
      viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
        @Suppress("DEPRECATION")
        override fun onGlobalLayout() {
          if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
          } else {
            viewTreeObserver.removeGlobalOnLayoutListener(this)
          }
          visibility = View.GONE
        }
      })
    }
  }

  private fun toggleResultView() {
    if (installedAppPackages.isNotEmpty()) {
      adapter.setItems(installedAppPackages)
      adapter.updateAppFilters(appFilter)
      if (filter_edittext.text.toString().isNotEmpty()) {
        adapter.updateAppFilter(filter_edittext.text.toString())
      }
      showNoItemView(adapter.itemCount == 0)
      recyclerview.overScrollMode = View.OVER_SCROLL_ALWAYS
    }

    ViewCompat.animate(progressbar).alpha(0f)
      .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong()).withEndAction {
        progressbar.visibility = View.INVISIBLE
      }.start()
  }

  private fun toggleFilterView() {
    if (filter_linearlayout.isVisible) {
      AnimationUtils.collapseView(filter_linearlayout)
      filter_edittext.text.clear()
      UiUtils.hideKeyboard(this)
    } else {
      setFilterChips(appFilter)
      AnimationUtils.expandView(filter_linearlayout)
    }
  }

  private fun addAppFilter(filterString: String) {
    appFilter.add(filterString)
    updateFilterIcon()
    sharedPreferencesHelper.saveFilters(appFilter)
    addFilterChip(filterString)
    filter_edittext.text.clear()
  }

  private fun removeAppFilter(chip: Chip) {
    flexbox_layout.removeView(chip)
    appFilter.remove(chip.text.toString())
    adapter.updateAppFilters(appFilter)
    showNoItemView(adapter.itemCount == 0)
    updateFilterIcon()
    sharedPreferencesHelper.saveFilters(appFilter)
  }

  private fun setFilterChips(filters: Collection<String>) {
    flexbox_layout.removeAllViews()
    for (i in filters) {
      addFilterChip(i)
    }
  }

  private fun addFilterChip(filterString: String) {
    (LayoutInflater.from(this).inflate(R.layout.filter_chip, flexbox_layout, false) as Chip).let {
      it.text = filterString
      it.isClickable = false
      flexbox_layout.addView(it)
      it.setOnCloseIconClickListener { view -> removeAppFilter(view as Chip) }
    }
  }

  private fun updateFilterIcon() {
    if (appFilter.size > 0) {
      fiter_imageview.setColorFilter(ResourcesCompat.getColor(resources, R.color.colorAccent, null))
    } else {
      fiter_imageview.clearColorFilter()
    }
  }

  private fun openAppSettingsActivity() {
    adapter.getSelectedPackageName()?.let {
      Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, "package:$it".toUri()).apply {
        flags = flags or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_SINGLE_TOP
      }.let { intent -> startActivity(intent) }
    }
  }

  private fun showNoItemView(show: Boolean) {
    no_items_textview.visibility = if (show) View.VISIBLE else View.INVISIBLE
  }
}