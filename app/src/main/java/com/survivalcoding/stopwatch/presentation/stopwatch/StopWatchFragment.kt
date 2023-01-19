package com.survivalcoding.stopwatch.presentation.stopwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.survivalcoding.stopwatch.R
import com.survivalcoding.stopwatch.databinding.FragmentStopWatchBinding
import com.survivalcoding.stopwatch.domain.util.secFormat
import com.survivalcoding.stopwatch.presentation.stopwatch.adapter.LabTimeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StopWatchFragment : Fragment() {


    private var _binding: FragmentStopWatchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StopWatchViewModel by viewModels()

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

        val recyclerLabTimes = binding.recyclerLabTimes
        val labTimeAdapter = LabTimeAdapter()
        recyclerLabTimes.adapter = labTimeAdapter
        recyclerLabTimes.setHasFixedSize(true)

        collectLatestRepeatOnLifeCycleStarted(viewModel.milSec) { milSec ->
            binding.milSecondTextView.text = String.format("%02d", (milSec % 1000) / 10)
            binding.secondTextView.text = secFormat(milSec / 1000)
        }

        collectLatestRepeatOnLifeCycleStarted(viewModel.allDeltaLabTimes) { deltaLabTimes ->
            labTimeAdapter.submitList(deltaLabTimes.reversed().toMutableList())
            recyclerLabTimes?.scrollToPosition(0)
        }

        collectLatestRepeatOnLifeCycleStarted(viewModel.isPlaying) { isPlaying ->

            if (isPlaying) {
                binding.milSecondTextView.clearAnimation()
                binding.secondTextView.clearAnimation()

                binding.playPauseButtonView.setImageResource(R.drawable.ic_baseline_pause_24)
                binding.borderCircle.setColorFilter(
                    ContextCompat.getColor(this.requireContext(), R.color.blue)
                )
                binding.motionLayout.transitionToEnd()
            } else {
                binding.milSecondTextView.clearAnimation()
                binding.secondTextView.clearAnimation()

                if (viewModel.isPlayedOneMore.value) {
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

        collectLatestRepeatOnLifeCycleStarted(viewModel.isPlayedOneMore) { isPlayedOneMore ->
            if (isPlayedOneMore) {
                binding.recordButtonView.isVisible = true
                binding.initButtonView.isVisible = true
            } else {
                binding.recordButtonView.isVisible = false
                binding.initButtonView.isVisible = false
                binding.milSecondTextView.clearAnimation()
                binding.secondTextView.clearAnimation()
            }
        }

        binding.playPauseButtonView.setOnClickListener {
            if (viewModel.isPlaying.value) viewModel.timerStop() else viewModel.timerPlay()
        }

        binding.initButtonView.setOnClickListener {
            viewModel.clear()
        }
        binding.recordButtonView.setOnClickListener {
            viewModel.record()
        }

    }

    private fun <T> collectLatestRepeatOnLifeCycleStarted(state: Flow<T>, block: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                state.collectLatest {
                    block(it)
                }
            }
        }
    }

    private fun <T> collectLatestRepeatOnLifeCycleStarted(state: StateFlow<T>, block: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                state.collectLatest {
                    block(it)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveLatestMilSec()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}