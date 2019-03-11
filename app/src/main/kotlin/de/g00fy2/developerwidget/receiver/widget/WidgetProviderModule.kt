package de.g00fy2.developerwidget.receiver.widget

import dagger.Binds
import dagger.Module
import de.g00fy2.developerwidget.receiver.widget.WidgetProviderContract.WidgetProvider
import de.g00fy2.developerwidget.receiver.widget.WidgetProviderContract.WidgetProviderPresenter

@Module
abstract class WidgetProviderModule {

  @Binds
  abstract fun provideWidgetProvider(widgetProviderImpl: WidgetProviderImpl): WidgetProvider

  @Binds
  abstract fun provideWidgetProviderPresenter(presenter: WidgetProviderPresenterImpl): WidgetProviderPresenter
}