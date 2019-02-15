package de.g00fy2.developerwidget.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import de.g00fy2.developerwidget.DevWidgetApp
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, ActivityBindingModule::class])
interface AppComponent : AndroidInjector<DevWidgetApp> {

  @Component.Builder
  abstract class Builder : AndroidInjector.Builder<DevWidgetApp>()
}