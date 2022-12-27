package com.survivalcoding.stopwatch

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
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
        val blinkAnim = AnimationUtils.loadAnimation(this, R.anim.blink_animation)

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
        viewModel.liveProgressPercent.observe(this){ percent ->
            binding.progressiveTimerButton.progress= percent
        }

        binding.startPauseButton.setOnClickListener {
            if (viewModel.isPaused) {
                stopAnimation(blinkAnim)
                binding.startPauseButton.setImageResource(R.drawable.ic_baseline_pause_24)
                viewModel.start()
            } else {
                startAnimation(blinkAnim)
                // TODO Progressive bar 추가
                binding.startPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                viewModel.pause()
            }

        }
        binding.resetButton.setOnClickListener {
            stopAnimation(blinkAnim)
            binding.startPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            viewModel.reset()
        }
        binding.recordButton.setOnClickListener {
            viewModel.lapTime()
        }
    }

    private fun startAnimation(anim: Animation) {
        binding.timeLayout.startAnimation(anim)
        binding.millisecondText.startAnimation(anim)
    }

    private fun stopAnimation(anim: Animation) {
        binding.timeLayout.clearAnimation()
        binding.millisecondText.clearAnimation()
    }

}