package de.g00fy2.developerwidget.di

import android.content.Context
import dagger.Binds
import dagger.Module
import de.g00fy2.developerwidget.DevWidgetApp
import de.g00fy2.developerwidget.utils.APPLICATION
import javax.inject.Named
import javax.inject.Singleton

@Module
abstract class AppModule {

  @Singleton
  @Binds
  @Named(APPLICATION)
  abstract fun provideApplicationContext(application: DevWidgetApp): Context

}