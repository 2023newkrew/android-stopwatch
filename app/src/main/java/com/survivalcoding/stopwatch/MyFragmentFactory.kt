package com.survivalcoding.stopwatch

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

class MyFragmentFactory : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        when (loadFragmentClass(classLoader, className)) {
            MainFragment::class.java -> MainFragment(100)
            else -> super.instantiate(classLoader, className)
        }
}