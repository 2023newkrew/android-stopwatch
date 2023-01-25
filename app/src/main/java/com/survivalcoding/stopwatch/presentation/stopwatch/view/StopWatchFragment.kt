package com.survivalcoding.stopwatch.presentation.stopwatch.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.survivalcoding.stopwatch.presentation.main.view_model.MainViewModel
import com.survivalcoding.stopwatch.R
import com.survivalcoding.stopwatch.databinding.FragmentStopWatchBinding
import com.survivalcoding.stopwatch.presentation.stopwatch.adapter.LaptimeRecordAdapter
import com.survivalcoding.stopwatch.presentation.stopwatch.view_model.StopWatchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@AndroidEntryPoint
class StopWatchFragment : Fragment(R.layout.fragment_stop_watch) {

    private var _binding: FragmentStopWatchBinding? = null
    private val binding get() = _binding!!
    private val df00 = DecimalFormat("00")
    private val mainViewModel: MainViewModel by activityViewModels()
    private val stopWatchViewModel: StopWatchViewModel by viewModels()
    private lateinit var lapTimeRecordAdapter: LaptimeRecordAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStopWatchBinding.inflate(inflater, container, false)
        val view = binding.root
        lapTimeRecordAdapter = LaptimeRecordAdapter(view.context)
        val linearLayoutManager = LinearLayoutManager(view.context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.recordRecyclerView.layoutManager = linearLayoutManager
        binding.recordRecyclerView.adapter = lapTimeRecordAdapter
        binding.recordRecyclerView.setHasFixedSize(true)

        val blinkAnim = AnimationUtils.loadAnimation(context, R.anim.blink_animation)

        if(mainViewModel.stopWatchUiState.isWorking && !mainViewModel.stopWatchUiState.isPaused){
            toggleButtonAnimation()
        }


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                stopWatchViewModel.lapTimeList.collectLatest {
                    if (it.isNotEmpty()) {
                        binding.progressiveTimerButtonWrapper?.transitionToEnd()
                    }
                    lapTimeRecordAdapter.submitList(it.toMutableList()) {
                        binding.recordRecyclerView.scrollToPosition(lapTimeRecordAdapter.itemCount - 1)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.mainUiState.collectLatest { state ->
                    if (state.getHour() > 0) {
                        binding.hourText.text = "${state.getHour()}"
                        binding.hourColon.text = ":"
                    } else {
                        binding.hourText.text = ""
                        binding.hourColon.text = ""
                    }
                    if (state.getMin() > 0) {
                        binding.minuteText.text = "${state.getMin()}"
                        binding.minuteColon.text = ":"
                    } else {
                        binding.minuteText.text = ""
                        binding.minuteColon.text = ""
                    }
                    binding.secondText.text = df00.format(state.getSec())
                    binding.millisecondText.text = df00.format(state.getMilliSec())
                }

            }
        }


        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                stopWatchViewModel.stopWatchUiState.collectLatest {
                    if(it.isWorking) {
                        if (it.isPaused) {
                            startAnimation(blinkAnim)
                            binding.startPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                            binding.recordButton.isVisible = false
                        } else {
                            stopAnimation()
                            binding.startPauseButton.setImageResource(R.drawable.ic_baseline_pause_24)
                            binding.recordButton.isVisible = true
                        }
                        binding.resetButton.isVisible = true
                    }
                    else{
                        binding.resetButton.isVisible = false
                        binding.recordButton.isVisible = false
                    }
                }
            }
        }



        binding.startPauseButton.setOnClickListener {
            if(stopWatchViewModel.stopWatchUiState.value.isPaused){
                stopWatchViewModel.start()
                mainViewModel.timerStart()
            }
            else {
                stopWatchViewModel.pause()
                mainViewModel.timerPause()
            }
            toggleButtonAnimation()
        }
        binding.resetButton.setOnClickListener {
            stopAnimation()
            binding.startPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            binding.startPauseMotion?.transitionToStart()
            if (stopWatchViewModel.stopWatchRecord.standardLapTime>0) {
                binding.progressiveTimerButtonWrapper?.transitionToStart()
            }
            mainViewModel.timeReset()
            stopWatchViewModel.reset()
        }
        binding.recordButton.setOnClickListener {
            stopWatchViewModel.lapTime(mainViewModel.mainUiState.value.time)
            if (stopWatchViewModel.stopWatchRecord.standardLapTime>0) {
                binding.progressiveTimerButtonWrapper?.transitionToEnd()
            }
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

    private fun toggleButtonAnimation(){
        if (binding.startPauseMotion?.progress == 0.0F) {
            binding.startPauseMotion?.transitionToEnd()
        } else {
            binding.startPauseMotion?.transitionToStart()
        }
    }

    override fun onStop() {
        stopWatchViewModel.onStoppedAction()
        super.onStop()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}