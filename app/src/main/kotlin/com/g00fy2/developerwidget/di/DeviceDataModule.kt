package com.g00fy2.developerwidget.di

import com.g00fy2.developerwidget.data.DeviceDataSource
import com.g00fy2.developerwidget.data.DeviceDataSourceImpl
import com.g00fy2.developerwidget.data.WidgetsSettingsDataSource
import com.g00fy2.developerwidget.data.WidgetsSettingsDataSourceImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DeviceDataModule {

  @Binds
  @Singleton
  abstract fun providesDeviceDataModule(deviceDataSourceImpl: DeviceDataSourceImpl): DeviceDataSource

  @Binds
  @Singleton
  abstract fun providesWidgetsSettingsDataSource(widgetsSettingsDataSourceImpl: WidgetsSettingsDataSourceImpl): WidgetsSettingsDataSource
}