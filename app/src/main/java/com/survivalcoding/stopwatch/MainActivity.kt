package com.survivalcoding.stopwatch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.survivalcoding.stopwatch.Config.Companion.KEY_IS_RUNNING
import com.survivalcoding.stopwatch.Config.Companion.KEY_TEXT_MILLI_SEC
import com.survivalcoding.stopwatch.Config.Companion.KEY_TEXT_SEC
import com.survivalcoding.stopwatch.Config.Companion.KEY_TIME
import com.survivalcoding.stopwatch.Config.Companion.PERIOD_TIMER
import com.survivalcoding.stopwatch.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var timer: Timer
    private var time: Long = 0L
    private var isRunning: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.playButton.setOnClickListener {
            if (isRunning) pause()
            else play()
            isRunning = !isRunning
        }
        binding.refreshButton.setOnClickListener {

        }
        binding.lapButton.setOnClickListener {

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putLong(KEY_TIME, time)
        outState.putBoolean(KEY_IS_RUNNING, isRunning)
        outState.putString(KEY_TEXT_SEC, binding.secTextView.text.toString())
        outState.putString(KEY_TEXT_MILLI_SEC, binding.milliSecTextView.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        time = savedInstanceState.getLong(KEY_TIME)
        isRunning = savedInstanceState.getBoolean(KEY_IS_RUNNING)
        if (isRunning) play()

        binding.secTextView.text = savedInstanceState.getString(KEY_TEXT_SEC)
        binding.milliSecTextView.text = savedInstanceState.getString(KEY_TEXT_MILLI_SEC)
    }

    private fun play() {
        binding.playButton.setImageResource(R.drawable.icon_pause)
        timer = kotlin.concurrent.timer(period = PERIOD_TIMER) {
            time += PERIOD_TIMER
            val milliSecond = time % 1000
            val second = (time / 1000) % 60
            val minute = (time / (1000 * 60)) % 60
            val hour = (time / (1000 * 60 * 60)) % 24

            // TODO time lose 예방을 위해 coroutine or handler 등 다른 방법 고려
            runOnUiThread {
                binding.secTextView.text =
                    if (hour > 0) String.format("%d:%02d:%02d", hour, minute, second)
                    else if (minute > 0) String.format("%02d:%02d", minute, second)
                    else second.toString()
                binding.milliSecTextView.text = String.format("%02d", milliSecond / 10)
            }
        }
    }

    private fun pause() {
        binding.playButton.setImageResource(R.drawable.icon_play)
        timer.cancel()
    }
}