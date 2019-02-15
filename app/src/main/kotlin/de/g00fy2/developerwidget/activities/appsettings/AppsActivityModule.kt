package de.g00fy2.developerwidget.activities.appsettings

import dagger.Binds
import dagger.Module
import de.g00fy2.developerwidget.base.BaseActivity
import de.g00fy2.developerwidget.di.ActivityModule
import de.g00fy2.developerwidget.di.annotations.ActivityScope

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
}