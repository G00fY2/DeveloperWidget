package de.g00fy2.developerwidget.di

import dagger.Binds
import dagger.Module
import de.g00fy2.developerwidget.controllers.SharedPreferenceController
import de.g00fy2.developerwidget.controllers.SharedPreferenceControllerImpl
import de.g00fy2.developerwidget.di.annotations.ActivityScope

@Module
abstract class ControllerModule {

  @Binds
  @ActivityScope
  abstract fun provideSharedPreferenceController(sharedPreferenceControllerImpl: SharedPreferenceControllerImpl): SharedPreferenceController

}