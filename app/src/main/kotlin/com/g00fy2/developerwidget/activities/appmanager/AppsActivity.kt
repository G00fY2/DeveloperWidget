package com.g00fy2.developerwidget.activities.appmanager

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.widget.TooltipCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.R.string
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.base.BaseContract.BasePresenter
import com.g00fy2.developerwidget.utils.AnimationUtils
import com.g00fy2.developerwidget.utils.DIALOG_ACTIVITY_HEIGHT_FACTOR
import com.g00fy2.developerwidget.utils.DIALOG_ACTIVITY_WIDTH_FACTOR
import com.g00fy2.developerwidget.utils.UiUtils
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_apps.*
import kotlinx.android.synthetic.main.app_filter_info.*
import javax.inject.Inject

class AppsActivity : BaseActivity(R.layout.activity_apps), AppsContract.AppsView {

  @Inject
  lateinit var presenter: AppsContract.AppsPresenter

  private lateinit var adapter: AppsAdapter
  private var scrollToTopAfterCommit = false
  private val clearDrawable by lazy {
    ResourcesCompat.getDrawable(resources, R.drawable.ic_clear, null)?.apply {
      setColorFilter(ResourcesCompat.getColor(resources, R.color.vectorTintColor, null), PorterDuff.Mode.SRC_IN)
      setBounds(0, 0, this.intrinsicWidth, this.intrinsicHeight)
    }
  }

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
      if (scrollToTopAfterCommit) {
        scrollToTopAfterCommit = false
        recyclerview.scrollToPosition(0)
      }
    })
    recyclerview.setHasFixedSize(true)
    recyclerview.itemAnimator = null
    recyclerview.layoutManager = LinearLayoutManager(this)
    recyclerview.adapter = adapter
    initFilterViews()
  }

  private fun initFilterViews() {
    app_filter_info.setOnClickListener {
      scrollToTopAfterCommit = true
      presenter.disableFilter()
      AnimationUtils.collapseView(app_filter_info, true)
    }
    TooltipCompat.setTooltipText(filter_imageview, filter_imageview.contentDescription)
    filter_imageview.setOnClickListener {
      presenter.updateFilter(null)
      toggleFilterView()
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
          setCompoundDrawables(null, null, if (!s.isNullOrEmpty()) clearDrawable else null, null)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
      })
      setOnTouchListener { v, event ->
        var consumed = false
        if (v is EditText) {
          if (event.x >= v.width - v.totalPaddingRight) {
            if (event.action == MotionEvent.ACTION_UP) {
              filter_edittext.text.clear()
            }
            consumed = true
          }
        }
        consumed
      }
    }
    // necessary to measure the view heights for animations
    setFilterChips(presenter.getCurrentFilter())
    filter_linearlayout.doOnPreDraw { it.visibility = View.GONE }
    app_filter_info.doOnPreDraw { it.visibility = View.GONE }
  }

  override fun toggleResultView(installedAppPackages: List<AppInfo>, filters: List<String>) {
    adapter.initialList(installedAppPackages, filters)
    show_all_textview.text = getString(string.show_all_apps).format(installedAppPackages.size)
    app_filter_info.visibility = if (filters.isEmpty() || installedAppPackages.isEmpty()) View.GONE else View.VISIBLE
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
      if (presenter.getCurrentFilter().isNotEmpty()) AnimationUtils.expandView(app_filter_info, true)
      AnimationUtils.collapseView(filter_linearlayout)
      UiUtils.hideKeyboard(this)
    } else {
      if (app_filter_info.isVisible) AnimationUtils.collapseView(app_filter_info, true)
      filter_edittext.text.clear()
      setFilterChips(presenter.getCurrentFilter())
      AnimationUtils.expandView(filter_linearlayout)
    }
  }

  private fun removeAppFilter(chip: Chip) {
    chip_group.removeView(chip)
    scrollToTopAfterCommit = true
    presenter.removeAppFilter(chip.text.toString(), filter_edittext.text.toString())
  }

  private fun setFilterChips(filters: Collection<String>) {
    chip_group.removeAllViews()
    for (i in filters) {
      addFilterChip(i)
    }
  }

  private fun addFilterChip(filterString: String) {
    (LayoutInflater.from(this).inflate(R.layout.filter_chip, chip_group, false) as Chip).let {
      it.text = filterString
      it.isClickable = false
      chip_group.addView(it)
      it.setOnCloseIconClickListener { view -> removeAppFilter(view as Chip) }
    }
  }
}