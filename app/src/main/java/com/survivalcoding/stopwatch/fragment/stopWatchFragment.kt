package com.survivalcoding.stopwatch.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.survivalcoding.stopwatch.MainViewModel
import com.survivalcoding.stopwatch.R
import com.survivalcoding.stopwatch.databinding.FragmentStopWatchBinding
import java.text.DecimalFormat

class stopWatchFragment : Fragment() {

    private var _binding: FragmentStopWatchBinding? = null
    private val binding get() = _binding!!
    private val df00 = DecimalFormat("00")
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStopWatchBinding.inflate(inflater, container, false)
        val view = binding.root
        val blinkAnim = AnimationUtils.loadAnimation(context, R.anim.blink_animation)
        buttonRecover(blinkAnim)
        viewModel.liveStateData.observe(viewLifecycleOwner) { state ->
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

        viewModel.liveProgressPercent.observe(viewLifecycleOwner) { percent ->
            binding.progressiveTimerButton.progress = percent
        }

        binding.startPauseButton.setOnClickListener {
            viewModel.isWorking = true
            binding.resetButton.isVisible = true
            if (viewModel.isPaused) {
                stopAnimation()
                binding.startPauseButton.setImageResource(R.drawable.ic_baseline_pause_24)
                viewModel.start()
                binding.recordButton.isVisible = true
            } else {
                startAnimation(blinkAnim)
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
            stopAnimation()
            binding.startPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            binding.startPauseMotion?.transitionToStart()
            viewModel.reset()
            binding.resetButton.visibility = View.INVISIBLE
            binding.recordButton.visibility = View.INVISIBLE
            viewModel.isWorking = false
        }
        binding.recordButton.setOnClickListener {
            viewModel.lapTime()
        }
        return view
    }

    private fun startAnimation(anim: Animation) {
        binding.timeLayout.startAnimation(anim)
        binding.millisecondText.startAnimation(anim)
    }

    private fun stopAnimation() {
        binding.timeLayout.clearAnimation()
        binding.millisecondText.clearAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun buttonRecover(blinkAnim: Animation) {
        if (viewModel.isWorking) {
            binding.resetButton.isVisible = true
            if (!viewModel.isPaused) {
                stopAnimation()
                binding.startPauseButton.setImageResource(R.drawable.ic_baseline_pause_24)
                binding.recordButton.isVisible = true
                binding.startPauseMotion?.transitionToEnd()
            } else {
                startAnimation(blinkAnim)
                binding.startPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                binding.recordButton.isVisible = false
                binding.startPauseMotion?.transitionToStart()
            }
        }

    }
}