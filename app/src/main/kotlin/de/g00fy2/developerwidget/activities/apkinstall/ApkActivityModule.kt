package de.g00fy2.developerwidget.activities.apkinstall

import dagger.Binds
import dagger.Module
import de.g00fy2.developerwidget.base.BaseActivity
import de.g00fy2.developerwidget.di.ActivityModule
import de.g00fy2.developerwidget.di.annotations.ActivityScope

@Module(includes = [ActivityModule::class])
abstract class ApkActivityModule {

  @ActivityScope
  @Binds
  abstract fun provideActivity(activity: ApkActivity): BaseActivity

  @ActivityScope
  @Binds
  abstract fun provideApkView(activity: ApkActivity): ApkContract.ApkView

  @ActivityScope
  @Binds
  abstract fun provideApkPresenter(presenter: ApkPresenterImpl): ApkContract.ApkPresenter

}