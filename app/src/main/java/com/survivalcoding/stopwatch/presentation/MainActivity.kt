package com.survivalcoding.stopwatch.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.survivalcoding.stopwatch.R
import com.survivalcoding.stopwatch.databinding.ActivityMainBinding
import com.survivalcoding.stopwatch.presentation.blank.BlankFragment
import com.survivalcoding.stopwatch.presentation.stopwatch.StopWatchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = binding.root
        setContentView(view)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<BlankFragment>(R.id.container)
            }
        }


        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.stopwatch_page -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<StopWatchFragment>(R.id.container)

                    }
                }

                else -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<BlankFragment>(R.id.container)

                    }
                }
            }
            return@setOnItemSelectedListener true
        }
    }


}