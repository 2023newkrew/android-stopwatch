package com.survivalcoding.stopwatch

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {
    private var isPaused = true
    private var timerWork: Timer? = null
    private var time = 0
    private var hour_text: TextView? = null
    private var minute_text: TextView? = null
    private var second_text: TextView? = null
    private var millisecond_text: TextView? = null
    private var start_pause_button: ImageButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hour_text = findViewById<TextView>(R.id.hour_text)
        minute_text = findViewById<TextView>(R.id.minute_text)
        second_text = findViewById<TextView>(R.id.second_text)
        millisecond_text = findViewById<TextView>(R.id.millisecond_text)
        start_pause_button = findViewById<ImageButton>(R.id.start_pause_button)
        if (savedInstanceState == null) {
            second_text?.text = "00"
            millisecond_text?.text = "00"
        }
        start_pause_button?.setOnClickListener {
            if (isPaused) {
                start()
            } else {
                pause()
            }

        }
    }
    private fun start() {

        start_pause_button?.setImageResource(R.drawable.ic_baseline_pause_24)	// 시작버튼을 일시정지 이미지로 변경
        isPaused = false

        timerWork = kotlin.concurrent.timer(period = 10) {	// timer() 호출
            time++	// period=10, 0.01초마다 time를 1씩 증가
            //val minute = if (time > 6000) time / 6000 else -1
            val sec = time / 100 % 60	// time/100, 나눗셈의 몫 (초 부분)
            val milli = time % 100	// time%100, 나눗셈의 나머지 (밀리초 부분)

            // UI조작을 위한 메서드
            runOnUiThread {
                second_text?.text = "$sec"	// TextView 세팅
                millisecond_text?.text = "$milli"	// Textview 세팅
            }
        }
    }
    private fun pause() {
        start_pause_button?.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        isPaused = true

        timerWork?.cancel()
    }
}