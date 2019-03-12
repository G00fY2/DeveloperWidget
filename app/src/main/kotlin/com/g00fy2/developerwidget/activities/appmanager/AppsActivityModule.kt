package com.g00fy2.developerwidget.activities.appmanager

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
abstract class AppsActivityModule {

  @ActivityScope
  @Binds
  abstract fun provideActivity(activity: AppsActivity): BaseActivity

  @ActivityScope
  @Binds
  abstract fun provideAppsView(activity: AppsActivity): AppsContract.AppsView

  @ActivityScope
  @Binds
  abstract fun provideAppsPresenter(activity: AppsPresenterImpl): AppsContract.AppsPresenter

  @ActivityScope
  @Binds
  abstract fun provideAppInfoBuilder(appInfoBuilder: AppInfo.AppInfoBuilderImpl): AppInfo.AppInfoBuilder

  @Module
  companion object {

    @ActivityScope
    @JvmStatic
    @Provides
    @Named(WIDGET_ID)
    fun provideWidgetId(activity: AppsActivity): String {
      return (activity.intent.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        ?: AppWidgetManager.INVALID_APPWIDGET_ID).toString()
    }
  }
}