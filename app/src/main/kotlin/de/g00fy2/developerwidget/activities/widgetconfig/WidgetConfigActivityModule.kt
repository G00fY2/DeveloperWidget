package de.g00fy2.developerwidget.activities.widgetconfig

import dagger.Binds
import dagger.Module
import de.g00fy2.developerwidget.base.BaseActivity
import de.g00fy2.developerwidget.di.ActivityModule
import de.g00fy2.developerwidget.di.annotations.ActivityScope

@Module(includes = [ActivityModule::class])
abstract class WidgetConfigActivityModule {

  @ActivityScope
  @Binds
  abstract fun provideActivity(activity: WidgetConfigActivity): BaseActivity

  @ActivityScope
  @Binds
  abstract fun provideWidgetConfigView(activity: WidgetConfigActivity): WidgetConfigContract.WidgetConfigView

  @ActivityScope
  @Binds
  abstract fun provideWidgetConfigPresenter(presenter: WidgetConfigPresenterImpl): WidgetConfigContract.WidgetConfigPresenter
}