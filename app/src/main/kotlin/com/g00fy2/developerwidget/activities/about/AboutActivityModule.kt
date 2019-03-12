package com.g00fy2.developerwidget.activities.about

import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.di.ActivityModule
import com.g00fy2.developerwidget.di.annotations.ActivityScope
import dagger.Binds
import dagger.Module

@Module(includes = [ActivityModule::class])
abstract class AboutActivityModule {

  @ActivityScope
  @Binds
  abstract fun provideActivity(activity: AboutActivity): BaseActivity

  @ActivityScope
  @Binds
  abstract fun provideAboutView(activity: AboutActivity): AboutContract.AboutView

  @ActivityScope
  @Binds
  abstract fun provideAboutPresenter(presenter: AboutPresenterImpl): AboutContract.AboutPresenter
}