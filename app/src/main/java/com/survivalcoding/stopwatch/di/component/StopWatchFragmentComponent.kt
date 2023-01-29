package com.survivalcoding.stopwatch.di.component

import com.survivalcoding.stopwatch.di.scope.FragmentScope
import com.survivalcoding.stopwatch.presentation.view.fragment.StopWatchFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface StopWatchFragmentComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): StopWatchFragmentComponent
    }

    fun inject(fragment: StopWatchFragment)
}