package com.survivalcoding.stopwatch

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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

        viewModel.liveStateData.observe(this) { state ->
            if (state.hour > 0) {
                binding.hourText.text = "${state.hour}"
                binding.hourColon.text = ":"
            } else {
                binding.hourText.text = ""
                binding.hourColon.text = ""
            }
            if (state.minute > 0) {
                binding.minuteText.text = "${state.minute}"
                binding.minuteColon.text = ":"
            } else {
                binding.minuteText.text = ""
                binding.minuteColon.text = ""
            }
            binding.secondText.text = df00.format(state.sec)
            binding.millisecondText.text = df00.format(state.milliSec)
        }

        viewModel.liveProgressPercent.observe(this) { percent ->
            binding.progressiveTimerButton.progress = percent
        }

        binding.startPauseButton.setOnClickListener {
            binding.resetButton.isVisible = true
            if (viewModel.isPaused) {
                stopAnimation(blinkAnim)
                binding.startPauseButton.setImageResource(R.drawable.ic_baseline_pause_24)
                viewModel.start()
                binding.recordButton.isVisible = true
            } else {
                startAnimation(blinkAnim)
                // TODO Progressive bar 추가
                binding.startPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                viewModel.pause()
                binding.recordButton.isVisible = false
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