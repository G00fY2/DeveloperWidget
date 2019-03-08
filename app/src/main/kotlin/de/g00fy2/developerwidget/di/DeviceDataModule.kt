package de.g00fy2.developerwidget.di

import dagger.Binds
import dagger.Module
import de.g00fy2.developerwidget.data.DeviceDataSource
import de.g00fy2.developerwidget.data.DeviceDataSourceImpl
import de.g00fy2.developerwidget.di.annotations.ActivityScope
import javax.inject.Singleton

@Module
abstract class DeviceDataModule {

  @Binds
  @Singleton
  abstract fun providesDeviceDataModule(deviceDataSourceImpl: DeviceDataSourceImpl): DeviceDataSource
}