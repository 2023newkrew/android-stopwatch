package com.survivalcoding.stopwatch

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import com.survivalcoding.stopwatch.databinding.ActivityMainBinding
import java.text.DecimalFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: MainViewModel by viewModels()
    val df00 = DecimalFormat("00")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        viewModel.liveHourData.observe(this) { hour ->
            if (hour > 0) {
                binding.hourText.text = "$hour"
                binding.hourColon.text = ":"
            } else {
                binding.hourText.text = ""
                binding.hourColon.text = ""
            }

        }
        viewModel.liveMinuteData.observe(this) { minute ->
            if (minute > 0) {
                binding.minuteText.text = "$minute"
                binding.minuteColon.text = ":"
            } else {
                binding.minuteText.text = ""
                binding.minuteColon.text = ""
            }
        }
        viewModel.liveSecData.observe(this) { sec ->
            binding.secondText.text = df00.format(sec)
        }
        viewModel.liveMilliSecData.observe(this) { milliSec ->
            binding.millisecondText.text = df00.format(milliSec)
        }

        binding.startPauseButton.setOnClickListener {
            if (viewModel.isPaused) {
                binding.startPauseButton.setImageResource(R.drawable.ic_baseline_pause_24)
                viewModel.start()
            } else {
                // TODO 시간 깜빡거리게 하기
                binding.startPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                viewModel.pause()
            }

        }
        binding.resetButton.setOnClickListener {
            binding.startPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            viewModel.reset()
        }
        binding.recordButton.setOnClickListener {

        }
    }

}