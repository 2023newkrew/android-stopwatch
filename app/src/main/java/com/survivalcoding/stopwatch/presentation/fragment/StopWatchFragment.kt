package com.survivalcoding.stopwatch.presentation.fragment

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.survivalcoding.stopwatch.Config
import com.survivalcoding.stopwatch.Config.Companion.KEY_BACKUP_TIME
import com.survivalcoding.stopwatch.Config.Companion.KEY_PREFS
import com.survivalcoding.stopwatch.Config.Companion.KEY_PROGRESS_MAX
import com.survivalcoding.stopwatch.Config.Companion.KEY_TIME
import com.survivalcoding.stopwatch.PrefsController
import com.survivalcoding.stopwatch.R
import com.survivalcoding.stopwatch.databinding.FragmentStopWatchBinding
import com.survivalcoding.stopwatch.presentation.viewmodel.MainViewModel

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

        // observe time live data
        viewModel.timeLiveData.observe(viewLifecycleOwner) { time ->
            val split = TimeSplit(time)
            binding.secTextView.text =
                if (split.hour > 0) String.format(
                    "%d:%02d:%02d",
                    split.hour,
                    split.minute,
                    split.second
                )
                else if (split.minute > 0) String.format("%02d:%02d", split.minute, split.second)
                else split.second.toString()
            binding.milliSecTextView.text = String.format("%02d", split.milliSecond / 10)

            viewModel.backupTime?.let {
                binding.timeProgressBar?.progress =
                    (binding.timeProgressBar?.max ?: 0).coerceAtMost((time - it).toInt())
            }
        }

        // set on click listener
        binding.playButton.setOnClickListener {
            if (viewModel.runningLiveData.value == true) {
                viewModel.pause()
                binding.playButton.setImageResource(R.drawable.icon_play)
                binding.lapButton.isVisible = false
                binding.timeLayout.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.animation_blink
                    )
                )
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

            binding.scrollView.visibility = View.GONE
            binding.lapLayout.removeAllViews()
            viewModel.logArrayList = ArrayList()
            requireContext().getSharedPreferences(KEY_PREFS, MODE_PRIVATE)
                .edit()
                .clear()
                .apply()
        }
        binding.lapButton.setOnClickListener {
            viewModel.timeLiveData.value?.let {
                if (viewModel.backupTime == null) {
                    binding.timeProgressBar?.max = it.toInt()
                    binding.checkBackProgressBar?.max = it.toInt()
                    binding.checkFrontProgressBar?.max = it.toInt()
                    binding.scrollView.isVisible = true
                } else {
                    val progress = binding.timeProgressBar?.progress ?: 0
                    val max = binding.timeProgressBar?.max ?: 0

                    binding.checkBackProgressBar?.progress = progress
                    binding.checkFrontProgressBar?.progress =
                        progress - (max * Config.THICK_CHECKER).toInt()
                    binding.timeProgressBar?.progress = 0
                }
                viewModel.backupTime = it

                viewModel.logArrayList.add(it)
                val index = viewModel.logArrayList.size
                val splitRight = TimeSplit(it)
                val logRight = String.format(
                    "%01d %02d.%02d",
                    splitRight.minute,
                    splitRight.second,
                    splitRight.milliSecond / 10
                )
                val logLeft =
                    if (index == 1) {
                        logRight
                    } else {
                        val gap = it - viewModel.logArrayList[index - 2]
                        val splitLeft = TimeSplit(gap)
                        String.format(
                            "%01d %02d.%02d",
                            splitLeft.minute,
                            splitLeft.second,
                            splitLeft.milliSecond / 10
                        )
                    }
                val log = "# ${String.format("%02d", index)}   $logLeft   $logRight"
                val textView = TextView(requireContext()).apply {
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    text = log
                    textSize = 16f
                    setLineSpacing(0f, 1.5f)
                }
                binding.lapLayout.addView(textView, 0)
            }
        }
    }


    override fun onResume() {
        super.onResume()

        // restore primitive data
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

        // restore custom data
        viewModel.logArrayList = PrefsController(requireContext()).restoreLogArrayList()
        if (viewModel.logArrayList.size > 0) binding.scrollView.isVisible = true
        for (i in viewModel.logArrayList.indices) {
            val time = viewModel.logArrayList[i]
            val splitRight = TimeSplit(time)
            val logRight = String.format(
                "%01d %02d.%02d",
                splitRight.minute,
                splitRight.second,
                splitRight.milliSecond / 10
            )
            val logLeft =
                if (i == 0) {
                    logRight
                } else {
                    val gap = time - viewModel.logArrayList[i - 1]
                    val splitLeft = TimeSplit(gap)
                    String.format(
                        "%01d %02d.%02d",
                        splitLeft.minute,
                        splitLeft.second,
                        splitLeft.milliSecond / 10
                    )
                }
            val log = "# ${String.format("%02d", i + 1)}   $logLeft   $logRight"
            val textView = TextView(requireContext()).apply {
                setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                text = log
                textSize = 16f
                setLineSpacing(0f, 1.5f)
            }
            binding.lapLayout.addView(textView, 0)
        }

        if (viewModel.runningLiveData.value == true) binding.playButton.setImageResource(R.drawable.icon_pause)
        else if ((viewModel.timeLiveData.value ?: 0) > 0) binding.timeLayout.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), R.anim.animation_blink)
        )
    }

    override fun onPause() {
        super.onPause()

        // backup primitive data
        val prefs = requireContext().getSharedPreferences(KEY_PREFS, MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putLong(KEY_TIME, viewModel.timeLiveData.value ?: 0L)
        editor.putLong(KEY_BACKUP_TIME, viewModel.backupTime ?: 0L)
        editor.putInt(KEY_PROGRESS_MAX, binding.timeProgressBar?.max ?: 0)
        editor.apply()

        // restore custom data
        PrefsController(requireContext()).putLogArrayList(viewModel.logArrayList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private data class TimeSplit(
        val time: Long
    ) {
        val milliSecond: Long by lazy { time % 1000 }
        val second: Long by lazy { (time / 1000) % 60 }
        val minute: Long by lazy { (time / (1000 * 60)) % 60 }
        val hour: Long by lazy { (time / (1000 * 60 * 60)) % 24 }
    }
}