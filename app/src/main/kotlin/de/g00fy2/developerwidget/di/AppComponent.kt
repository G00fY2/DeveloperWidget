package de.g00fy2.developerwidget.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import de.g00fy2.developerwidget.DevWidgetApp
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, ActivityBindingModule::class])
interface AppComponent : AndroidInjector<DevWidgetApp> {

  @Component.Builder
  abstract class Builder : AndroidInjector.Builder<DevWidgetApp>()
}