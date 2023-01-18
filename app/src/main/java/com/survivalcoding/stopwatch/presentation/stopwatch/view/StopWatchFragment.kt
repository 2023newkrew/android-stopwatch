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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.survivalcoding.stopwatch.presentation.main.view_model.MainViewModel
import com.survivalcoding.stopwatch.R
import com.survivalcoding.stopwatch.databinding.FragmentStopWatchBinding
import com.survivalcoding.stopwatch.presentation.stopwatch.adapter.LaptimeRecordAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DecimalFormat
@AndroidEntryPoint
class StopWatchFragment : Fragment() {

    private var _binding: FragmentStopWatchBinding? = null
    private val binding get() = _binding!!
    private val df00 = DecimalFormat("00")
    private val viewModel: MainViewModel by activityViewModels()
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
        recover(blinkAnim)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.lapTimeLists.collectLatest {
                    if (it.isNotEmpty()) {
                        binding.progressiveTimerButtonWrapper?.transitionToEnd()
                    }
                    lapTimeRecordAdapter.submitList(it.toMutableList()) {
                        binding.recordRecyclerView.scrollToPosition(lapTimeRecordAdapter.itemCount - 1)
                    }
                }
            }
        }


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
            if (viewModel.standardLapTime > 0) {
                binding.progressiveTimerButtonWrapper?.transitionToStart()
            }
            viewModel.reset()
            binding.resetButton.isVisible = false
            binding.recordButton.isVisible = false
            viewModel.isWorking = false

        }
        binding.recordButton.setOnClickListener {
            viewModel.lapTime()
            if (viewModel.standardLapTime > 0) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun recover(blinkAnim: Animation) {
        if (viewModel.isWorking) {
            viewModel.initTime()
            binding.resetButton.isVisible = true
            if (!viewModel.isPaused) {
                stopAnimation()
                binding.startPauseButton.setImageResource(R.drawable.ic_baseline_pause_24)
                binding.recordButton.isVisible = true
                binding.startPauseMotion?.transitionToEnd()
                //viewModel.start() // TODO: 화면 회전 부분 고려해야 함
            } else {
                startAnimation(blinkAnim)
                binding.startPauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                binding.recordButton.isVisible = false
                binding.startPauseMotion?.transitionToStart()
            }
//            if (viewModel.recordList.size > 0) {
//                binding.progressiveTimerButtonWrapper?.transitionToEnd()
//                laptimeRecordAdapter.submitList(viewModel.recordList.toMutableList())
//            }
        }


    }
}