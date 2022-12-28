package com.survivalcoding.stopwatch

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.survivalcoding.stopwatch.databinding.ActivityMainBinding
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: MainViewModel by viewModels()
    private val df00 = DecimalFormat("00")


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
        viewModel.liveProgressPercent.observe(this) { percent ->
            binding.progressiveTimerButton.progress = percent
        }


        binding.startPauseButton.setOnClickListener {
            binding.resetButton.visibility = View.VISIBLE
            if (viewModel.isPaused) {
                stopAnimation(blinkAnim)
                binding.startPauseButton.setImageResource(R.drawable.ic_baseline_pause_24)
                viewModel.start()
                binding.recordButton.visibility = View.VISIBLE
            } else {
                startAnimation(blinkAnim)
                // TODO Progressive bar 추가
                binding.startPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                viewModel.pause()
                binding.recordButton.visibility = View.INVISIBLE
            }
            if (binding.startPauseMotion?.progress == 0.0F) {
                binding.startPauseMotion?.transitionToEnd()
            } else {
                binding.startPauseMotion?.transitionToStart()
            }

        }
        binding.resetButton.setOnClickListener {
            stopAnimation(blinkAnim)
            binding.startPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            binding.startPauseMotion?.transitionToStart()
            viewModel.reset()
            binding.resetButton.visibility = View.INVISIBLE
            binding.recordButton.visibility = View.INVISIBLE
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