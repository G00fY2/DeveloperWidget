package com.g00fy2.developerwidget.activities.apkinstall

import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.di.ActivityModule
import com.g00fy2.developerwidget.di.annotations.ActivityScope
import dagger.Binds
import dagger.Module

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

  @ActivityScope
  @Binds
  abstract fun provideApkFileBuilder(apkFileBuilder: ApkFile.ApkFileBuilderImpl): ApkFile.ApkFileBuilder
}