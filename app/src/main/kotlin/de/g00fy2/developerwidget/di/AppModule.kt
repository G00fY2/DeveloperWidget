package de.g00fy2.developerwidget.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule
import de.g00fy2.developerwidget.DevWidgetApp
import javax.inject.Named
import javax.inject.Singleton

@Module
abstract class AppModule {

  @Singleton
  @Binds
  @Named("application")
  abstract fun provideApplicationContext(application: DevWidgetApp): Context

}