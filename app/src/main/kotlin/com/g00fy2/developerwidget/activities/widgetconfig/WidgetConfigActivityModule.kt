package com.g00fy2.developerwidget.activities.widgetconfig

import android.appwidget.AppWidgetManager
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.di.ActivityModule
import com.g00fy2.developerwidget.di.annotations.ActivityScope
import com.g00fy2.developerwidget.utils.WIDGET_ID
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [ActivityModule::class])
abstract class WidgetConfigActivityModule {

  @Binds
  @ActivityScope
  abstract fun provideActivity(activity: WidgetConfigActivity): BaseActivity

  @Binds
  @ActivityScope
  abstract fun provideWidgetConfigView(activity: WidgetConfigActivity): WidgetConfigContract.WidgetConfigView

  @Binds
  @ActivityScope
  abstract fun provideWidgetConfigPresenter(presenter: WidgetConfigPresenterImpl): WidgetConfigContract.WidgetConfigPresenter

  @Module
  companion object {

    @JvmStatic
    @Provides
    @ActivityScope
    @Named(WIDGET_ID)
    fun provideWidgetId(activity: WidgetConfigActivity): String {
      return (activity.intent.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        ?: AppWidgetManager.INVALID_APPWIDGET_ID).toString()
    }
  }
}