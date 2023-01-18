package com.survivalcoding.stopwatch.presentation.stopwatch

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.survivalcoding.stopwatch.R
import com.survivalcoding.stopwatch.databinding.FragmentStopWatchBinding
import com.survivalcoding.stopwatch.presentation.stopwatch.adapter.LaptimeRecordAdapter
import com.survivalcoding.stopwatch.presentation.stopwatch.state.MainUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@AndroidEntryPoint
class StopWatchFragment : Fragment() {

    private var _binding: FragmentStopWatchBinding? = null
    private val binding get() = _binding!!
    private val df00 = DecimalFormat("00")
    private val viewModel: StopWatchViewModel by viewModels()
    private lateinit var lapTimeRecordAdapter: LaptimeRecordAdapter
    private val blinkAnim by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.blink_animation)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStopWatchBinding.inflate(inflater, container, false)
        val view = binding.root

        // recyclerView Adapter 세팅
        lapTimeRecordAdapter = LaptimeRecordAdapter(view.context)
        val linearLayoutManager = LinearLayoutManager(view.context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.recordRecyclerView.layoutManager = linearLayoutManager
        binding.recordRecyclerView.adapter = lapTimeRecordAdapter
        binding.recordRecyclerView.setHasFixedSize(true)


        binding.startPauseButton.setOnClickListener {
            startPauseAction()
        }

        binding.resetButton.setOnClickListener {
            resetAction()
        }

        binding.recordButton.setOnClickListener {
            recordAction()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 일시정지 시에 blink(깜빡임) 애니메이션 적용
        recover(blinkAnim)

        // lapTime 가져오기
        lifecycleScope.launch {
            viewModel.getLapTimeRecords().collect { recordList ->
                if (recordList.isNotEmpty()) {
                    binding.progressiveTimerButtonWrapper?.transitionToEnd()
                }
                lapTimeRecordAdapter.submitList(recordList) {
                    binding.recordRecyclerView.scrollToPosition(
                        lapTimeRecordAdapter.itemCount - 1
                    )
                }
            }
        }

        // Main Time UI 업데이트
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mainUiState.collect {
                    updateTimeUi(it)
                }
            }
        }

        // progress bar 업데이트
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.progressPercentState.collect {
                    if (Build.VERSION.SDK_INT >= 24) {
                        binding.progressiveTimerButton.setProgress(it.percent, true)
                    } else {
                        binding.progressiveTimerButton.progress = it.percent
                    }
                }
            }
        }
    }

    override fun onStop() {
        viewModel.onStoppedAction()
        super.onStop()
    }

    private fun startPauseAction() {
        viewModel.setStopWatchState(isWorking = true)
        binding.resetButton.isVisible = true
        if (viewModel.stopWatchState.isPaused) {
            stopAnimation()
            binding.startPauseButton.setImageResource(R.drawable.ic_baseline_pause_24)
            viewModel.stopWatchStart()
            binding.recordButton.isVisible = true
        } else {
            startAnimation(blinkAnim)
            binding.startPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            viewModel.stopWatchPause()
            binding.recordButton.isVisible = false
        }
        if (binding.startPauseMotion?.progress == 0.0F) {
            binding.startPauseMotion?.transitionToEnd()
        } else {
            binding.startPauseMotion?.transitionToStart()
        }
    }

    private fun resetAction() {
        stopAnimation()
        binding.startPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        binding.startPauseMotion?.transitionToStart()
        if (viewModel.stopWatchState.standardLapTime > 0) {
            binding.progressiveTimerButtonWrapper?.transitionToStart()
        }
        viewModel.stopWatchReset()
        binding.resetButton.isVisible = false
        binding.recordButton.isVisible = false
        viewModel.setStopWatchState(isWorking = false)
    }

    private fun updateTimeUi(state: MainUiState) {
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

    private fun recordAction() {
        viewModel.lapTime()
        if (viewModel.stopWatchState.standardLapTime > 0) {
            binding.progressiveTimerButtonWrapper?.transitionToEnd()
        }
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

    private fun recover(blinkAnim: Animation) {
        if (viewModel.stopWatchState.isWorking) {
            viewModel.initTime()
            binding.resetButton.isVisible = true
            if (!viewModel.stopWatchState.isPaused) {
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