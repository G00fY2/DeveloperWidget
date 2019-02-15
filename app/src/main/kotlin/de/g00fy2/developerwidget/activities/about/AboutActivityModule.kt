package de.g00fy2.developerwidget.activities.about

import dagger.Binds
import dagger.Module
import de.g00fy2.developerwidget.base.BaseActivity
import de.g00fy2.developerwidget.di.ActivityModule
import de.g00fy2.developerwidget.di.annotations.ActivityScope

@Module(includes = [ActivityModule::class])
abstract class AboutActivityModule {

  @ActivityScope
  @Binds
  abstract fun provideActivity(activity: AboutActivity): BaseActivity

  @ActivityScope
  @Binds
  abstract fun provideAboutView(activity: AboutActivity): AboutContract.AboutView

  @ActivityScope
  @Binds
  abstract fun provideAboutPresenter(presenter: AboutPresenterImpl): AboutContract.AboutPresenter
}