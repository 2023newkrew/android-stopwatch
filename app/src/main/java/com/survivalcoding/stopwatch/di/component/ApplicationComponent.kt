package com.survivalcoding.stopwatch.di.component

import com.survivalcoding.stopwatch.di.module.MainActivityModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MainActivityModule::class])
interface ApplicationComponent {
    fun mainActivityComponent(): MainActivityComponent.Factory
}