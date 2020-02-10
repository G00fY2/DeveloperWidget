package com.g00fy2.developerwidget.di

import com.g00fy2.developerwidget.controllers.DayNightController
import com.g00fy2.developerwidget.controllers.DayNightControllerImpl
import com.g00fy2.developerwidget.controllers.IntentController
import com.g00fy2.developerwidget.controllers.IntentControllerImpl
import com.g00fy2.developerwidget.controllers.PermissionController
import com.g00fy2.developerwidget.controllers.PermissionControllerImpl
import com.g00fy2.developerwidget.controllers.StringController
import com.g00fy2.developerwidget.controllers.StringControllerImpl
import com.g00fy2.developerwidget.controllers.ToastController
import com.g00fy2.developerwidget.controllers.ToastControllerImpl
import com.g00fy2.developerwidget.controllers.WidgetPreferenceController
import com.g00fy2.developerwidget.controllers.WidgetPreferenceControllerImpl
import com.g00fy2.developerwidget.di.annotations.ActivityScope
import dagger.Binds
import dagger.Module
import dagger.Reusable
import javax.inject.Singleton

@Module
abstract class GlobalControllerModule {

  @Binds
  @Reusable
  abstract fun providesDayNightController(dayNightControllerImpl: DayNightControllerImpl): DayNightController

  @Binds
  @Singleton
  abstract fun provideToastController(toastControllerImpl: ToastControllerImpl): ToastController
}

@Module
abstract class ActivityControllerModule {

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
}