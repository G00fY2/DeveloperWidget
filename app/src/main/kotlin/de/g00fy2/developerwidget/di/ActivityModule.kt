package de.g00fy2.developerwidget.di

import android.content.Context
import dagger.Binds
import dagger.Module
import de.g00fy2.developerwidget.base.BaseActivity
import de.g00fy2.developerwidget.di.annotations.ActivityScope
import de.g00fy2.developerwidget.utils.ACTIVITY
import javax.inject.Named

@Module(includes = [ControllerModule::class])
abstract class ActivityModule {

  @Binds
  @ActivityScope
  @Named(ACTIVITY)
  abstract fun provideActivityContext(activity: BaseActivity): Context
}