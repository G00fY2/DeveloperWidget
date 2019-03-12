package com.g00fy2.developerwidget.activities.apkinstall

import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.di.ActivityModule
import com.g00fy2.developerwidget.di.annotations.ActivityScope
import dagger.Binds
import dagger.Module

@Module(includes = [ActivityModule::class])
abstract class ApkActivityModule {

  @Binds
  @ActivityScope
  abstract fun provideActivity(activity: ApkActivity): BaseActivity

  @Binds
  @ActivityScope
  abstract fun provideApkView(activity: ApkActivity): ApkContract.ApkView

  @Binds
  @ActivityScope
  abstract fun provideApkPresenter(presenter: ApkPresenterImpl): ApkContract.ApkPresenter

  @Binds
  @ActivityScope
  abstract fun provideApkFileBuilder(apkFileBuilder: ApkFile.ApkFileBuilderImpl): ApkFile.ApkFileBuilder
}