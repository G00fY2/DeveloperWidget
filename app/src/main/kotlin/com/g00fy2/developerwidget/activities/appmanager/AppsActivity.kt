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
import com.g00fy2.developerwidget.databinding.ActivityAppsBinding
import com.g00fy2.developerwidget.ktx.collapse
import com.g00fy2.developerwidget.ktx.expand
import com.g00fy2.developerwidget.ktx.hideKeyboard
import com.google.android.material.chip.Chip
import javax.inject.Inject

class AppsActivity : BaseActivity(true), AppsContract.AppsView {

  @Inject
  lateinit var presenter: AppsContract.AppsPresenter

  override val binding: ActivityAppsBinding by viewBinding(ActivityAppsBinding::inflate)
  private lateinit var adapter: AppsAdapter
  private var scrollToTopAfterCommit = false
  private val clearDrawable by lazy { initClearDrawable() }

  override fun providePresenter(): BasePresenter = presenter

  override fun initView() {
    adapter = AppsAdapter()
    adapter.setOnAppClicked { appInfo -> presenter.openAppSettingsActivity(appInfo) }
    adapter.setCommitCallback(Runnable {
      adapter.itemCount.let {
        binding.noItemsTextview.visibility = if (it == 0) View.VISIBLE else View.INVISIBLE
        binding.noItemsImageview.visibility = if (it == 0) View.VISIBLE else View.INVISIBLE
        binding.recyclerview.overScrollMode = if (it == 0) View.OVER_SCROLL_NEVER else View.OVER_SCROLL_ALWAYS
      }
      if (scrollToTopAfterCommit) {
        scrollToTopAfterCommit = false
        binding.recyclerview.scrollToPosition(0)
      }
    })
    binding.recyclerview.setHasFixedSize(true)
    binding.recyclerview.itemAnimator = null
    binding.recyclerview.layoutManager = LinearLayoutManager(this)
    binding.recyclerview.adapter = adapter

    binding.cancelTextview.setOnClickListener { finish() }
    initFilterViews()
  }

  private fun initFilterViews() {
    binding.appFilterInfo.setOnClickListener {
      scrollToTopAfterCommit = true
      presenter.disableFilter()
      binding.appFilterInfo.collapse(true, LinearOutSlowInInterpolator())
    }
    TooltipCompat.setTooltipText(binding.filterImageview, binding.filterImageview.contentDescription)
    binding.filterImageview.setOnClickListener {
      presenter.updateFilter(null)
      toggleFilterView()
    }

    binding.filterEdittext.apply {
      filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
        if (source.isEmpty() || source.matches("^[a-zA-Z0-9._*]*$".toRegex())) {
          null
        } else {
          ""
        }
      })
      setOnEditorActionListener { _, actionId, _ ->
        val filterString = text.toString().trim()
        if (actionId == EditorInfo.IME_ACTION_DONE && filterString.isNotEmpty() && !presenter.duplicateFilter(
            filterString
          )
        ) {
          presenter.addAppFilter(filterString)
          addFilterChip(filterString)
          text.clear()
        }
        true
      }
      doAfterTextChanged {
        presenter.updateFilter(it)
        setCompoundDrawables(null, null, if (!it.isNullOrEmpty()) clearDrawable else null, null)
      }
      setOnTouchListener { _, event ->
        if (event.x >= width - totalPaddingRight) {
          if (event.action == MotionEvent.ACTION_UP) {
            performClick()
            text.clear()
          }
          return@setOnTouchListener true
        }
        false
      }
    }
    // necessary to measure the view heights for animations
    setFilterChips(presenter.getCurrentFilter())
    binding.filterLinearlayout.doOnPreDraw { it.visibility = View.GONE }
    binding.appFilterInfo.doOnPreDraw { it.visibility = View.GONE }
  }

  override fun toggleResultView(installedAppPackages: List<AppInfo>, filters: List<String>) {
    adapter.initialList(installedAppPackages, filters)
    binding.showAllTextview.text = getString(R.string.show_all_apps).format(installedAppPackages.size)
    binding.appFilterInfo.visibility =
      if (filters.isEmpty() || installedAppPackages.isEmpty()) View.GONE else View.VISIBLE
    if (binding.filterEdittext.text.toString().isNotEmpty()) {
      adapter.updateAppFilter(binding.filterEdittext.text.toString())
    }

    ViewCompat.animate(binding.progressbar).alpha(0f)
      .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
      .withEndAction { binding.progressbar.visibility = View.INVISIBLE }
      .start()
  }

  override fun updateAppFilter(filters: List<String>) = adapter.updateAppFilters(filters)

  override fun updateAppFilter(filter: String) = adapter.updateAppFilter(filter)

  override fun updateFilterIcon(filterActive: Boolean) {
    if (filterActive) {
      binding.filterImageview.setColorFilter(ResourcesCompat.getColor(resources, R.color.colorAccent, null))
    } else {
      binding.filterImageview.clearColorFilter()
    }
  }

  private fun toggleFilterView() {
    if (binding.filterLinearlayout.isVisible) {
      if (presenter.getCurrentFilter().isNotEmpty()) binding.appFilterInfo.expand(true, FastOutLinearInInterpolator())
      binding.filterLinearlayout.collapse(easing = FastOutLinearInInterpolator())
      binding.filterEdittext.hideKeyboard()
    } else {
      if (binding.appFilterInfo.isVisible) binding.appFilterInfo.collapse(true, LinearOutSlowInInterpolator())
      binding.filterEdittext.text.clear()
      setFilterChips(presenter.getCurrentFilter())
      binding.filterLinearlayout.expand(easing = LinearOutSlowInInterpolator())
    }
  }

  private fun removeAppFilter(chip: Chip) {
    binding.chipGroup.removeView(chip)
    scrollToTopAfterCommit = true
    presenter.removeAppFilter(chip.text.toString(), binding.filterEdittext.text.toString())
  }

  private fun setFilterChips(filters: Collection<String>) {
    binding.chipGroup.removeAllViews()
    for (i in filters) {
      addFilterChip(i)
    }
  }

  private fun addFilterChip(filterString: String) {
    (LayoutInflater.from(this).inflate(R.layout.filter_chip, binding.chipGroup, false) as Chip).let {
      it.text = filterString
      it.isClickable = false
      binding.chipGroup.addView(it)
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
      setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    }
  }
}