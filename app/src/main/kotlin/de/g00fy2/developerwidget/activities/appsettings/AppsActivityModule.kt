package de.g00fy2.developerwidget.activities.appsettings

import android.appwidget.AppWidgetManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import de.g00fy2.developerwidget.base.BaseActivity
import de.g00fy2.developerwidget.di.ActivityModule
import de.g00fy2.developerwidget.di.annotations.ActivityScope
import de.g00fy2.developerwidget.utils.WIDGET_ID
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