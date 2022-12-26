package com.survivalcoding.stopwatch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
            if (isRunning) {
                binding.playButton.setImageResource(R.drawable.icon_play)
                timer.cancel()
            } else {
                binding.playButton.setImageResource(R.drawable.icon_pause)
                timer = kotlin.concurrent.timer(period = PERIOD_TIMER) {
                    time += PERIOD_TIMER
                    val sec = time / 1000
                    val milliSec = (time % 1000) / PERIOD_TIMER
                    runOnUiThread {
                        binding.secTextView.text = sec.toString()
                        binding.milliSecTextView.text = String.format("%02d", milliSec)
                    }
                }
            }
            isRunning = !isRunning
        }
        binding.refreshButton.setOnClickListener {

        }
        binding.lapButton.setOnClickListener {

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }
}