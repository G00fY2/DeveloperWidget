package com.g00fy2.developerwidget.di

import android.content.Context
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.di.annotations.ActivityScope
import com.g00fy2.developerwidget.di.annotations.ACTIVITY
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module
abstract class ActivityModule {

  @Binds
  @ActivityScope
  @Named(ACTIVITY)
  abstract fun provideActivityContext(activity: BaseActivity): Context
}