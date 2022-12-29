package com.survivalcoding.stopwatch.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.survivalcoding.stopwatch.R
import com.survivalcoding.stopwatch.databinding.FragmentStopWatchBinding
import com.survivalcoding.stopwatch.viewmodel.MainViewModel


class StopWatchFragment : Fragment() {


    private var _binding: FragmentStopWatchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    private val blinkAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this.context, R.anim.blink_animation)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStopWatchBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.milSecLiveData.observe(this.viewLifecycleOwner) { milSec ->
            binding.milSecondTextView.text = String.format("%02d", (milSec % 1000) / 10)
            val sec = milSec / 1000
            val secText = String.format("%02d", sec % 60)
            val minuteText = if (sec >= 60) String.format("%02d:", (sec / 60) % 60) else ""
            val hourText = if (sec >= 3600) String.format("%02d:", (sec / 3600)) else ""
            val newText = hourText + minuteText + secText
            binding.secondTextView.text = newText
        }

        updateUI()
        binding.playPauseButtonView.setOnClickListener {
            if (viewModel.isPlaying) viewModel.timerStop() else viewModel.timerPlay()
            updateUI()
        }
    }

    private fun updateUI() {
        if (viewModel.isPlayedOneMore) {
            binding.recordButtonView.isVisible = true
            binding.initButtonView.isVisible = true
        }

        if (viewModel.isPlaying) {
            binding.milSecondTextView.clearAnimation()
            binding.secondTextView.clearAnimation()


            binding.playPauseButtonView.setImageResource(R.drawable.ic_baseline_pause_24)
            binding.borderCircle.setColorFilter(
                ContextCompat.getColor(this.requireContext(), R.color.blue)
            )
            binding.motionLayout.transitionToEnd()
        } else {

            if (viewModel.isPlayedOneMore) {
                binding.milSecondTextView.startAnimation(blinkAnim)
                binding.secondTextView.startAnimation(blinkAnim)
            }

            binding.playPauseButtonView.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            binding.borderCircle.setColorFilter(
                ContextCompat.getColor(this.requireContext(), R.color.gray)
            )
            binding.motionLayout.transitionToStart()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}