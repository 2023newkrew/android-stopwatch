package com.survivalcoding.stopwatch.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.survivalcoding.stopwatch.R
import com.survivalcoding.stopwatch.databinding.ActivityMainBinding
import com.survivalcoding.stopwatch.fragment.BlankFragment
import com.survivalcoding.stopwatch.fragment.StopWatchFragment

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = binding.root
        setContentView(view)




        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.stopwatch_page->{
                    if (savedInstanceState == null) {
                        supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            replace<StopWatchFragment>(R.id.container)

                        }
                    }
                }

                else->{
                    if (savedInstanceState == null) {
                        supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            replace<BlankFragment>(R.id.container)

                        }
                    }
                }
            }
            return@setOnItemSelectedListener true
        }
    }


}