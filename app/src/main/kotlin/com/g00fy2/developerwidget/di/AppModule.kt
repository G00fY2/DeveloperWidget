package com.g00fy2.developerwidget.di

import android.content.Context
import com.g00fy2.developerwidget.DevWidgetApp
import com.g00fy2.developerwidget.di.annotations.APPLICATION
import dagger.Binds
import dagger.Module
import javax.inject.Named
import javax.inject.Singleton

@Module
abstract class AppModule {

  @Binds
  @Singleton
  @Named(APPLICATION)
  abstract fun provideApplicationContext(application: DevWidgetApp): Context
}