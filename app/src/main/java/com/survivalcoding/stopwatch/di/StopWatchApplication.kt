package com.survivalcoding.stopwatch.di

import android.app.Application
import com.survivalcoding.stopwatch.di.component.ApplicationComponent
import com.survivalcoding.stopwatch.di.component.DaggerApplicationComponent

class StopWatchApplication : Application() {
    val appComponent: ApplicationComponent = DaggerApplicationComponent.create()
}