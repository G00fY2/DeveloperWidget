package com.g00fy2.developerwidget.activities.appmanager

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import android.view.inputmethod.EditorInfo
import androidx.annotation.ContentView
import androidx.appcompat.widget.TooltipCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.base.BaseContract.BasePresenter
import com.g00fy2.developerwidget.utils.AnimationUtils
import com.g00fy2.developerwidget.utils.DIALOG_ACTIVITY_HEIGHT_FACTOR
import com.g00fy2.developerwidget.utils.DIALOG_ACTIVITY_WIDTH_FACTOR
import com.g00fy2.developerwidget.utils.UiUtils
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_apps.*
import javax.inject.Inject

@ContentView(R.layout.activity_apps)
class AppsActivity : BaseActivity(), AppsContract.AppsView {

  @Inject lateinit var presenter: AppsContract.AppsPresenter

  private lateinit var adapter: AppsAdapter

  override fun providePresenter(): BasePresenter = presenter

  override fun onCreate(savedInstanceState: Bundle?) {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    super.onCreate(savedInstanceState)

    val width = (resources.displayMetrics.widthPixels * DIALOG_ACTIVITY_WIDTH_FACTOR).toInt()
    val height = (resources.displayMetrics.heightPixels * DIALOG_ACTIVITY_HEIGHT_FACTOR).toInt()
    window.setLayout(width, height)

    cancel_textview.setOnClickListener { finish() }

    adapter = AppsAdapter()
    adapter.setOnAppClicked { appInfo -> presenter.openAppSettingsActivity(appInfo) }
    adapter.setCommitCallback(Runnable {
      adapter.itemCount.let {
        no_items_textview.visibility = if (it == 0) View.VISIBLE else View.INVISIBLE
        no_items_imageview.visibility = if (it == 0) View.VISIBLE else View.INVISIBLE
        recyclerview.overScrollMode = if (it == 0) View.OVER_SCROLL_NEVER else View.OVER_SCROLL_ALWAYS
      }
    })
    recyclerview.setHasFixedSize(true)
    recyclerview.itemAnimator = null
    recyclerview.layoutManager = LinearLayoutManager(this)
    recyclerview.adapter = adapter
    initFilterViews()
  }

  override fun onBackPressed() {
    if (filter_linearlayout.isVisible) {
      filter_edittext.text.clear()
      toggleFilterView()
    } else {
      super.onBackPressed()
    }
  }

  private fun initFilterViews() {
    TooltipCompat.setTooltipText(filter_imageview, filter_imageview.contentDescription)
    filter_imageview.setOnClickListener { toggleFilterView() }

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
        if (actionId == EditorInfo.IME_ACTION_DONE && filterString.isNotEmpty() && !presenter.duplicateFilter(
            filterString
          )
        ) {
          presenter.addAppFilter(filterString)
          addFilterChip(filterString)
          filter_edittext.text.clear()
        }
        true
      }
      addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
          presenter.updateFilter(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
      })
    }
    // necessary to properly measure the filter view height
    setFilterChips(presenter.getCurrentFilter())
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

  override fun toggleResultView(installedAppPackages: List<AppInfo>, filters: List<String>) {
    adapter.initialList(installedAppPackages, filters)
    if (filter_edittext.text.toString().isNotEmpty()) {
      adapter.updateAppFilter(filter_edittext.text.toString())
    }

    ViewCompat.animate(progressbar).alpha(0f)
      .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong()).withEndAction {
        progressbar.visibility = View.INVISIBLE
      }.start()
  }

  override fun updateAppFilter(filters: List<String>) {
    adapter.updateAppFilters(filters)
  }

  override fun updateAppFilter(filter: String) {
    adapter.updateAppFilter(filter)
  }

  override fun updateFilterIcon(filterActive: Boolean) {
    if (filterActive) {
      filter_imageview.setColorFilter(ResourcesCompat.getColor(resources, R.color.colorAccent, null))
    } else {
      filter_imageview.clearColorFilter()
    }
  }

  private fun toggleFilterView() {
    if (filter_linearlayout.isVisible) {
      AnimationUtils.collapseView(filter_linearlayout)
      UiUtils.hideKeyboard(this)
    } else {
      setFilterChips(presenter.getCurrentFilter())
      AnimationUtils.expandView(filter_linearlayout)
    }
  }

  private fun removeAppFilter(chip: Chip) {
    flexbox_layout.removeView(chip)
    presenter.removeAppFilter(chip.text.toString())
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
}