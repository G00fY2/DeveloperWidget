package com.g00fy2.developerwidget.receiver.widget

import com.g00fy2.developerwidget.receiver.widget.WidgetProviderContract.WidgetProvider
import com.g00fy2.developerwidget.receiver.widget.WidgetProviderContract.WidgetProviderPresenter
import dagger.Binds
import dagger.Module

@Module
abstract class WidgetProviderModule {

  @Binds
  abstract fun provideWidgetProvider(widgetProviderImpl: WidgetProviderImpl): WidgetProvider

  @Binds
  abstract fun provideWidgetProviderPresenter(presenter: WidgetProviderPresenterImpl): WidgetProviderPresenter
}