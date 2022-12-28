package com.survivalcoding.stopwatch

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.survivalcoding.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val viewModel: MainViewModel by viewModels()

        viewModel.timeLiveData.observe(this) { time ->
            val milliSecond = time % 1000
            val second = (time / 1000) % 60
            val minute = (time / (1000 * 60)) % 60
            val hour = (time / (1000 * 60 * 60)) % 24

            binding.secTextView.text =
                if (hour > 0) String.format("%d:%02d:%02d", hour, minute, second)
                else if (minute > 0) String.format("%02d:%02d", minute, second)
                else second.toString()
            binding.milliSecTextView.text = String.format("%02d", milliSecond / 10)
        }

        binding.playButton.setOnClickListener {
            if (viewModel.isRunning) {
                viewModel.pause()
                binding.playButton.setImageResource(R.drawable.icon_play)
            } else {
                viewModel.play()
                binding.playButton.setImageResource(R.drawable.icon_pause)
            }
            viewModel.isRunning = !viewModel.isRunning
        }
    }
}