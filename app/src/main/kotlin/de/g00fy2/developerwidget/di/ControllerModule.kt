package de.g00fy2.developerwidget.di

import dagger.Binds
import dagger.Module
import de.g00fy2.developerwidget.controllers.IntentController
import de.g00fy2.developerwidget.controllers.IntentControllerImpl
import de.g00fy2.developerwidget.controllers.PermissionController
import de.g00fy2.developerwidget.controllers.PermissionControllerImpl
import de.g00fy2.developerwidget.controllers.SharedPreferenceController
import de.g00fy2.developerwidget.controllers.SharedPreferenceControllerImpl
import de.g00fy2.developerwidget.controllers.StringController
import de.g00fy2.developerwidget.controllers.StringControllerImpl
import de.g00fy2.developerwidget.controllers.ToastController
import de.g00fy2.developerwidget.controllers.ToastControllerImpl
import de.g00fy2.developerwidget.di.annotations.ActivityScope

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
  abstract fun provideSharedPreferenceController(sharedPreferenceControllerImpl: SharedPreferenceControllerImpl): SharedPreferenceController

  @Binds
  @ActivityScope
  abstract fun provideStringController(stringControllerImpl: StringControllerImpl): StringController

  @Binds
  @ActivityScope
  abstract fun provideToastController(toastControllerImpl: ToastControllerImpl): ToastController

}