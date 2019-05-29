package com.g00fy2.developerwidget.activities.shortcut

import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.di.ActivityModule
import com.g00fy2.developerwidget.di.annotations.ActivityScope
import dagger.Binds
import dagger.Module

@Module(includes = [ActivityModule::class])
abstract class CreateShortcutActivityModule {

  @Binds
  @ActivityScope
  abstract fun provideActivity(activity: CreateShortcutActivity): BaseActivity

  @Binds
  @ActivityScope
  abstract fun provideApkView(activity: CreateShortcutActivity): CreateShortcutContract.CreateShortcutView

  @Binds
  @ActivityScope
  abstract fun provideApkPresenter(presenter: CreateShortcutPresenterImpl): CreateShortcutContract.CreateShortcutPresenter
}