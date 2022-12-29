package com.survivalcoding.stopwatch.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.survivalcoding.stopwatch.R
import com.survivalcoding.stopwatch.databinding.FragmentStopWatchBinding
import com.survivalcoding.stopwatch.ui.MainViewModel

class StopWatchFragment : Fragment() {
    private var _binding: FragmentStopWatchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStopWatchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewModel: MainViewModel by activityViewModels()
        viewModel.timeLiveData.observe(viewLifecycleOwner) { time ->
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

        if (viewModel.runningLiveData.value == true) binding.playButton.setImageResource(R.drawable.icon_pause)
        else if ((viewModel.timeLiveData.value ?: 0) > 0) binding.timeLayout.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.animation_blink))

        binding.playButton.setOnClickListener {
            if (viewModel.runningLiveData.value == true) {
                viewModel.pause()
                binding.playButton.setImageResource(R.drawable.icon_play)
                binding.timeLayout.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.animation_blink))
                binding.lapButton.isVisible = false
                viewModel.runningLiveData.value = false
            } else {
                viewModel.play()
                binding.playButton.setImageResource(R.drawable.icon_pause)
                binding.timeLayout.clearAnimation()
                binding.refreshButton.isVisible = true
                binding.lapButton.isVisible = true
                viewModel.runningLiveData.value = true
            }
        }
        binding.refreshButton.setOnClickListener {
            viewModel.pause()
            viewModel.runningLiveData.value = false
            binding.playButton.setImageResource(R.drawable.icon_play)
            binding.timeLayout.clearAnimation()
            binding.refreshButton.isVisible = false
            binding.lapButton.isVisible = false

            viewModel.timeLiveData.postValue(0L)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}