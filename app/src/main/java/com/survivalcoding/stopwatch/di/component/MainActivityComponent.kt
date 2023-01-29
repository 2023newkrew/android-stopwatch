package com.survivalcoding.stopwatch.di.component

import com.survivalcoding.stopwatch.di.module.StopWatchFragmentModule
import com.survivalcoding.stopwatch.di.scope.ActivityScope
import com.survivalcoding.stopwatch.presentation.view.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [StopWatchFragmentModule::class])
interface MainActivityComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainActivityComponent
    }

    fun inject(activity: MainActivity)
    fun stopWatchFragmentComponent(): StopWatchFragmentComponent.Factory
}