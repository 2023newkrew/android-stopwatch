package com.survivalcoding.stopwatch.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.survivalcoding.stopwatch.R
import com.survivalcoding.stopwatch.databinding.ActivityMainBinding
import com.survivalcoding.stopwatch.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = binding.root
        setContentView(view)


        viewModel.milSecLiveData.observe(this) { milSec ->
            binding.milSecondTextView.text = "${(milSec % 1000) / 10}"
            val sec = milSec / 1000
            val secText = String.format("%02d", sec % 60)
            val minuteText = if (sec >= 60) String.format("%02d:", (sec / 60) % 60) else ""
            val hourText = if (sec >= 3600) String.format("%02d:", (sec / 3600)) else ""
            val newText = hourText + minuteText + secText
            binding.secondTextView.text = newText
        }

        // 최초 시작 버튼 클릭 시에 기록, 초기화 버튼 생기도록
        viewModel.isFirstPlayLiveData.observe(this) { isFirstPlay ->
            if (isFirstPlay) {
                binding.recordButtonView.visibility = VISIBLE
                binding.initButtonView.visibility = VISIBLE
            }
        }

        viewModel.isPlayingLiveData.observe(this) { isPlaying ->

            if (isPlaying) {
                binding.playPauseButtonView.setImageResource(R.drawable.ic_baseline_pause_24)
                binding.borderCircle?.setColorFilter(
                    ContextCompat.getColor(this, R.color.blue)
                )

            } else {
                binding.playPauseButtonView.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                binding.borderCircle?.setColorFilter(
                    ContextCompat.getColor(this, R.color.gray)
                )
            }


        }



        binding.playPauseButtonView.setOnClickListener {
            viewModel.playPauseBtnClicked()
        }
    }
}