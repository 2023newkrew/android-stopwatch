package com.survivalcoding.stopwatch.presentation.fragment

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.survivalcoding.stopwatch.Config.Companion.KEY_BACKUP_TIME
import com.survivalcoding.stopwatch.Config.Companion.KEY_PREFS
import com.survivalcoding.stopwatch.Config.Companion.KEY_PROGRESS_MAX
import com.survivalcoding.stopwatch.Config.Companion.KEY_TIME
import com.survivalcoding.stopwatch.PrefsController
import com.survivalcoding.stopwatch.R
import com.survivalcoding.stopwatch.databinding.FragmentStopWatchBinding
import com.survivalcoding.stopwatch.presentation.util.TimeSplit
import com.survivalcoding.stopwatch.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class StopWatchFragment : Fragment() {
    private var _binding: FragmentStopWatchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

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

        // observe state
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    val timeSplit = TimeSplit(state.time)

                    // update text view
                    binding.secTextView.text =
                        if (timeSplit.hour > 0) {
                            String.format(
                                "%d:%02d:%02d",
                                timeSplit.hour,
                                timeSplit.minute,
                                timeSplit.second
                            )
                        } else if (timeSplit.minute > 0) {
                            String.format(
                                "%02d:%02d",
                                timeSplit.minute,
                                timeSplit.second
                            )
                        } else {
                            timeSplit.second.toString()
                        }
                    binding.milliSecTextView.text =
                        String.format("%02d", timeSplit.milliSecond / 10)

                    // update time progress bar
                    binding.timeProgressBar?.max = state.timeProgressMax
                    state.backupTime?.let {
                        binding.timeProgressBar?.progress =
                            (binding.timeProgressBar?.max ?: 0)
                                .coerceAtMost((state.time - it).toInt())
                    }

                    // update check front progress bar
                    binding.checkFrontProgressBar?.let {
                        it.max = state.checkFrontProgressMax
                        it.progress = state.checkFrontProgress
                    }

                    // update check back progress bar
                    binding.checkBackProgressBar?.let {
                        it.max = state.checkBackProgressMax
                        it.progress = state.checkBackProgress
                    }

                    // update button image
                    binding.playButton.setImageResource(
                        if (state.isRunning) R.drawable.icon_pause
                        else R.drawable.icon_play
                    )

                    // update refresh button visibility
                    binding.refreshButton.isVisible = state.time != 0L

                    // update lap button visibility
                    binding.lapButton.isVisible = state.isRunning

                    // update scroll view visibility
                    binding.scrollView.visibility =
                        if (state.logList.isNotEmpty()) VISIBLE
                        else GONE

                    // update time layout animation
                    if (!state.isRunning && (state.time) > 0) {
                        binding.timeLayout.startAnimation(
                            AnimationUtils.loadAnimation(requireContext(), R.anim.animation_blink)
                        )
                    } else
                        binding.timeLayout.clearAnimation()

                    // update lap layout
                    if (state.logList.isEmpty()) {
                        binding.lapLayout.removeAllViews()
                        requireContext().getSharedPreferences(KEY_PREFS, MODE_PRIVATE)
                            .edit()
                            .clear()
                            .apply()
                    }
                }
            }
        }

        // set on click listener
        binding.playButton.setOnClickListener {
            if (viewModel.state.value.isRunning) viewModel.pause()
            else viewModel.play()
        }
        binding.refreshButton.setOnClickListener {
            viewModel.refresh()
        }
        binding.lapButton.setOnClickListener {
            viewModel.lap(
                binding.timeProgressBar?.progress ?: 0,
                binding.timeProgressBar?.max ?: 0
            )

            // add lap time log
            viewModel.addLog()
            addLapView(viewModel.getLogText())
        }
    }

    override fun onResume() {
        super.onResume()

        // restore primitive data
        val prefs = requireContext().getSharedPreferences(KEY_PREFS, MODE_PRIVATE)
        viewModel.initTime(prefs.getLong(KEY_TIME, 0L))
        viewModel.initBackupTime(
            if (prefs.getLong(KEY_BACKUP_TIME, 0L) == 0L) null
            else prefs.getLong(KEY_BACKUP_TIME, 0L)
        )
        viewModel.initProgressMax(prefs.getInt(KEY_PROGRESS_MAX, 0))

        // restore custom data
        viewModel.initLogList(PrefsController(requireContext()).restoreLogArrayList())
        for (i in viewModel.state.value.logList.indices) addLapView(viewModel.getLogText(i))
    }

    override fun onPause() {
        super.onPause()

        // backup primitive data
        val prefs = requireContext().getSharedPreferences(KEY_PREFS, MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putLong(KEY_TIME, viewModel.state.value.time)
        editor.putLong(KEY_BACKUP_TIME, viewModel.state.value.backupTime ?: 0L)
        editor.putInt(KEY_PROGRESS_MAX, binding.timeProgressBar?.max ?: 0)
        editor.apply()

        // backup custom data
        PrefsController(requireContext()).putLogArrayList(viewModel.state.value.logList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addLapView(string: String) {
        val textView = TextView(requireContext()).apply {
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            text = string
            textSize = 16f
            setLineSpacing(0f, 1.5f)
        }
        binding.lapLayout.addView(textView, 0)
    }
}