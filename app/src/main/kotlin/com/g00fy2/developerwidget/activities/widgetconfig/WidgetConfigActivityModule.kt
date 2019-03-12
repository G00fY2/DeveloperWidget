package com.g00fy2.developerwidget.activities.widgetconfig

import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.di.ActivityModule
import com.g00fy2.developerwidget.di.annotations.ActivityScope
import dagger.Binds
import dagger.Module

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
}