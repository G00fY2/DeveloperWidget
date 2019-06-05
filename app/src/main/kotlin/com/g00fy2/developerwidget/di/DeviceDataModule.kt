package com.g00fy2.developerwidget.di

import com.g00fy2.developerwidget.data.DeviceDataSource
import com.g00fy2.developerwidget.data.DeviceDataSourceImpl
import com.g00fy2.developerwidget.data.WidgetsPreferencesDataSource
import com.g00fy2.developerwidget.data.WidgetsPreferencesDataSourceImpl
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
  abstract fun providesWidgetsPreferencesDataSource(widgetsPreferencesDataSourceImpl: WidgetsPreferencesDataSourceImpl): WidgetsPreferencesDataSource
}