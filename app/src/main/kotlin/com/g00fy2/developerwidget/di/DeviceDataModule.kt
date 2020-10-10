package com.g00fy2.developerwidget.di

import com.g00fy2.developerwidget.data.DeviceDataSource
import com.g00fy2.developerwidget.data.DeviceDataSourceImpl
import com.g00fy2.developerwidget.data.WidgetsPreferencesDataSource
import com.g00fy2.developerwidget.data.WidgetsPreferencesDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
abstract class DeviceDataModule {

  @Binds
  @Reusable
  abstract fun providesDeviceDataModule(deviceDataSourceImpl: DeviceDataSourceImpl): DeviceDataSource

  @Binds
  @Reusable
  abstract fun providesWidgetsPreferencesDataSource(widgetsPreferencesDataSourceImpl: WidgetsPreferencesDataSourceImpl): WidgetsPreferencesDataSource
}