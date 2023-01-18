package com.survivalcoding.stopwatch.presentation.stopwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.survivalcoding.stopwatch.R
import com.survivalcoding.stopwatch.databinding.FragmentStopWatchBinding
import dagger.hilt.android.AndroidEntryPoint
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


        val secFormatLambda = { sec: Long ->
            val secText = String.format("%02d", sec % 60)
            val minuteText = if (sec >= 60) String.format(
                "%02d:",
                (sec / 60) % 60
            ) else ""
            val hourText = if (sec >= 3600) String.format(
                "%02d:",
                (sec / 3600)
            ) else ""
            hourText + minuteText + secText
        }

        val milSecFormatLambda = { milSec: Long ->
            String.format(
                "%01d:%02d:%02d",
                (milSec / 1000) / 60,
                (milSec / 1000) % 60,
                (milSec % 1000) / 10
            )
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel
            }
        }


        viewModel.milSecLiveData.observe(this.viewLifecycleOwner) { milSec ->
            binding.milSecondTextView.text = String.format("%02d", (milSec % 1000) / 10)
            binding.secondTextView.text = secFormatLambda(milSec / 1000)
        }

        viewModel.allLabTimes.observe(this.viewLifecycleOwner) { labTimes ->
            binding.labStack.removeAllViews()

            var preLabTime = 0L
            labTimes.forEachIndexed { index, labTime ->
                val textView = TextView(requireContext()).apply {
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_white))
                    text = String.format(
                        "랩 %d  ${milSecFormatLambda(labTime.time - preLabTime)} ${
                            milSecFormatLambda(labTime.time)
                        }", index + 1
                    )
                    textSize = 16f
                    setLineSpacing(0f, 1.5f)
                }
                binding.labStack.addView(textView, 0)
                preLabTime = labTime.time
            }
        }

        updateUI()

        binding.playPauseButtonView.setOnClickListener {
            if (viewModel.isPlaying) viewModel.timerStop() else viewModel.timerPlay()
            updateUI()
        }
        binding.initButtonView.setOnClickListener {
            viewModel.clear()
            updateUI()
        }
        binding.recordButtonView.setOnClickListener {
            viewModel.record()
            updateUI()
        }

    }

    private fun updateUI() {

        if (viewModel.isPlayedOneMore) {
            binding.recordButtonView.isVisible = true
            binding.initButtonView.isVisible = true
        } else {
            binding.recordButtonView.isVisible = false
            binding.initButtonView.isVisible = false
        }

        binding.milSecondTextView.clearAnimation()
        binding.secondTextView.clearAnimation()

        if (viewModel.isPlaying) {
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

    override fun onPause() {
        super.onPause()
        viewModel.saveLatestMilSec()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}