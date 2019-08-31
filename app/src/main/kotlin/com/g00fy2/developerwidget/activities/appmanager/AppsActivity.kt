package com.g00fy2.developerwidget.activities.appmanager

import android.graphics.BlendMode.SRC_IN
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.TooltipCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.base.BaseContract.BasePresenter
import com.g00fy2.developerwidget.ktx.collapse
import com.g00fy2.developerwidget.ktx.expand
import com.g00fy2.developerwidget.ktx.hideKeyboard
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_apps.*
import javax.inject.Inject

class AppsActivity : BaseActivity(R.layout.activity_apps, true), AppsContract.AppsView {

  @Inject
  lateinit var presenter: AppsContract.AppsPresenter

  private lateinit var adapter: AppsAdapter
  private var scrollToTopAfterCommit = false
  private val clearDrawable by lazy { initClearDrawable() }

  override fun providePresenter(): BasePresenter = presenter

  override fun initView() {
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

    cancel_textview.setOnClickListener { finish() }
    initFilterViews()
  }

  private fun initFilterViews() {
    app_filter_info.setOnClickListener {
      scrollToTopAfterCommit = true
      presenter.disableFilter()
      app_filter_info.collapse(true, LinearOutSlowInInterpolator())
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
      doAfterTextChanged {
        presenter.updateFilter(it)
        setCompoundDrawables(null, null, if (!it.isNullOrEmpty()) clearDrawable else null, null)
      }
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
    show_all_textview.text = getString(R.string.show_all_apps).format(installedAppPackages.size)
    app_filter_info.visibility = if (filters.isEmpty() || installedAppPackages.isEmpty()) View.GONE else View.VISIBLE
    if (filter_edittext.text.toString().isNotEmpty()) {
      adapter.updateAppFilter(filter_edittext.text.toString())
    }

    ViewCompat.animate(progressbar).alpha(0f)
      .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong()).withEndAction {
        progressbar.visibility = View.INVISIBLE
      }.start()
  }

  override fun updateAppFilter(filters: List<String>) = adapter.updateAppFilters(filters)

  override fun updateAppFilter(filter: String) = adapter.updateAppFilter(filter)

  override fun updateFilterIcon(filterActive: Boolean) {
    if (filterActive) {
      filter_imageview.setColorFilter(ResourcesCompat.getColor(resources, R.color.colorAccent, null))
    } else {
      filter_imageview.clearColorFilter()
    }
  }

  private fun toggleFilterView() {
    if (filter_linearlayout.isVisible) {
      if (presenter.getCurrentFilter().isNotEmpty()) app_filter_info.expand(true, FastOutLinearInInterpolator())
      filter_linearlayout.collapse(easing = FastOutLinearInInterpolator())
      filter_edittext.hideKeyboard()
    } else {
      if (app_filter_info.isVisible) app_filter_info.collapse(true, LinearOutSlowInInterpolator())
      filter_edittext.text.clear()
      setFilterChips(presenter.getCurrentFilter())
      filter_linearlayout.expand(easing = LinearOutSlowInInterpolator())
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

  private fun initClearDrawable(): Drawable? {
    return AppCompatResources.getDrawable(this, R.drawable.ic_clear)?.apply {
      if (VERSION.SDK_INT >= VERSION_CODES.Q) {
        colorFilter =
          BlendModeColorFilter(ResourcesCompat.getColor(resources, R.color.iconTintColor, null), SRC_IN)
      } else {
        @Suppress("DEPRECATION")
        setColorFilter(ResourcesCompat.getColor(resources, R.color.iconTintColor, null), PorterDuff.Mode.SRC_IN)
      }
      setBounds(0, 0, this.intrinsicWidth, this.intrinsicHeight)
    }
  }
}