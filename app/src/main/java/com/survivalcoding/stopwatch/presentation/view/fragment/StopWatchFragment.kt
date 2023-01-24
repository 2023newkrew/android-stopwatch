package com.survivalcoding.stopwatch.presentation.view.fragment

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.survivalcoding.stopwatch.Config.Companion.KEY_BACKUP_TIME
import com.survivalcoding.stopwatch.Config.Companion.KEY_PREFS
import com.survivalcoding.stopwatch.Config.Companion.KEY_PROGRESS_MAX
import com.survivalcoding.stopwatch.Config.Companion.KEY_TIME
import com.survivalcoding.stopwatch.Config.Companion.THICK_CHECKER
import com.survivalcoding.stopwatch.PrefsController
import com.survivalcoding.stopwatch.R
import com.survivalcoding.stopwatch.databinding.FragmentStopWatchBinding
import com.survivalcoding.stopwatch.domain.usecase.*
import com.survivalcoding.stopwatch.presentation.viewmodel.MainViewModel

class StopWatchFragment : Fragment() {
    private var _binding: FragmentStopWatchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStopWatchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val prefs = requireContext().getSharedPreferences(KEY_PREFS, MODE_PRIVATE)
        viewModel.timeLiveData.value = prefs.getLong(KEY_TIME, 0L)
        viewModel.backupTime =
            if (prefs.getLong(KEY_BACKUP_TIME, 0L) == 0L) null
            else prefs.getLong(KEY_BACKUP_TIME, 0L)
        prefs.getInt(KEY_PROGRESS_MAX, 0).let {
            binding.timeProgressBar?.max = it
            binding.checkBackProgressBar?.max = it
            binding.checkFrontProgressBar?.max = it
        }
        viewModel.logArrayList = PrefsController(requireContext()).restoreLogArrayList()
        if (viewModel.logArrayList.size > 0) binding.scrollView.isVisible = true
        for (i in viewModel.logArrayList.indices) {
            val timeList = viewModel.logArrayList
            val log = GetTimeLogUseCase(i, timeList)
            val textView = TextView(requireContext()).apply {
                setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                text = log
                textSize = 16f
                setLineSpacing(0f, 1.5f)
            }
            binding.lapLayout.addView(textView, 0)
        }

        viewModel.timeLiveData.observe(viewLifecycleOwner) { time ->
            binding.secTextView.text = GetSecStringUseCase(time)
            binding.milliSecTextView.text = GetMilliSecStringUseCase(time)

            viewModel.backupTime?.let {
                binding.timeProgressBar?.progress = (binding.timeProgressBar?.max ?: 0).coerceAtMost((time - it).toInt())
            }
        }

        if (viewModel.runningLiveData.value == true) binding.playButton.setImageResource(R.drawable.icon_pause)
        else if ((viewModel.timeLiveData.value ?: 0) > 0) binding.timeLayout.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.animation_blink))

        binding.playButton.setOnClickListener {
            if (viewModel.runningLiveData.value == true) {
                viewModel.pause()
                binding.playButton.setImageResource(R.drawable.icon_play)
                binding.lapButton.isVisible = false
                binding.timeLayout.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.animation_blink))
                viewModel.runningLiveData.value = false
            } else {
                viewModel.play()
                binding.playButton.setImageResource(R.drawable.icon_pause)
                binding.refreshButton.isVisible = true
                binding.lapButton.isVisible = true
                binding.timeLayout.clearAnimation()
                viewModel.runningLiveData.value = true
            }
        }
        binding.refreshButton.setOnClickListener {
            viewModel.pause()
            binding.playButton.setImageResource(R.drawable.icon_play)
            binding.refreshButton.isVisible = false
            binding.lapButton.isVisible = false
            viewModel.runningLiveData.value = false
            binding.timeLayout.clearAnimation()
            viewModel.timeLiveData.postValue(0L)

            viewModel.backupTime = null
            binding.timeProgressBar?.progress = 0
            binding.checkBackProgressBar?.progress = 0

            binding.scrollView.visibility = GONE
            binding.lapLayout.removeAllViews()
            viewModel.logArrayList = ArrayList()
            prefs.edit().clear().apply()
        }
        binding.lapButton.setOnClickListener {
            viewModel.timeLiveData.value?.let {nowTime ->
                if (viewModel.backupTime == null) {
                    binding.timeProgressBar?.max = nowTime.toInt()
                    binding.checkBackProgressBar?.max = nowTime.toInt()
                    binding.checkFrontProgressBar?.max = nowTime.toInt()
                    binding.scrollView.isVisible = true
                } else {
                    val progress = binding.timeProgressBar?.progress ?: 0
                    val max = binding.timeProgressBar?.max ?: 0

                    binding.checkBackProgressBar?.progress = progress
                    binding.checkFrontProgressBar?.progress = progress - (max * THICK_CHECKER).toInt()
                    binding.timeProgressBar?.progress = 0
                }
                viewModel.backupTime = nowTime

                viewModel.logArrayList.add(nowTime)
                val log = GetLastTimeLogUseCase(nowTime, viewModel.logArrayList)
                val textView = TextView(requireContext()).apply {
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    text = log
                    textSize = 16f
                    setLineSpacing(0f, 1.5f)
                }
                binding.lapLayout.addView(textView, 0)
            }
        }

        return root
    }

    override fun onPause() {
        super.onPause()

        // backup view model data
        val prefs = requireContext().getSharedPreferences(KEY_PREFS, MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putLong(KEY_TIME, viewModel.timeLiveData.value ?: 0L)
        editor.putLong(KEY_BACKUP_TIME, viewModel.backupTime ?: 0L)
        editor.putInt(KEY_PROGRESS_MAX, binding.timeProgressBar?.max ?: 0)
        PrefsController(requireContext()).putLogArrayList(viewModel.logArrayList)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}