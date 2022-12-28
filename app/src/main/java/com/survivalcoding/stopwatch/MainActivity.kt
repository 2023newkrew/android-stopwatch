package com.survivalcoding.stopwatch

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.survivalcoding.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = MyFragmentFactory()
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                val bundle = bundleOf("init22222" to 0, "name2" to "홍길동")
                setReorderingAllowed(true)
                add<MainFragment>(R.id.container, args = bundle)
                //

            }
        }


        viewModel.countLiveData.observe(this) { count ->
            binding.countTextView.text = "$count"
        }

        binding.addButton.setOnClickListener {
            viewModel.increase()
        }
    }
}