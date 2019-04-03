package com.g00fy2.developerwidget.di

import com.g00fy2.developerwidget.controllers.*
import com.g00fy2.developerwidget.di.annotations.ActivityScope
import dagger.Binds
import dagger.Module

@Module
abstract class ControllerModule {

  @Binds
  @ActivityScope
  abstract fun providesIntentController(intentControllerImpl: IntentControllerImpl): IntentController

  @Binds
  @ActivityScope
  abstract fun providePermissionController(permissionControllerImpl: PermissionControllerImpl): PermissionController

  @Binds
  @ActivityScope
  abstract fun provideWidgetPreferenceController(widgetPreferenceControllerImpl: WidgetPreferenceControllerImpl): WidgetPreferenceController

  @Binds
  @ActivityScope
  abstract fun provideStringController(stringControllerImpl: StringControllerImpl): StringController

  @Binds
  @ActivityScope
  abstract fun provideToastController(toastControllerImpl: ToastControllerImpl): ToastController

}