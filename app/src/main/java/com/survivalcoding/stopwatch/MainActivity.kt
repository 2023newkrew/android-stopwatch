package com.survivalcoding.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println("debug : $savedInstanceState")

        val addButton = findViewById<FloatingActionButton>(R.id.add_button)
        val countTextView : TextView = findViewById(R.id.count_text_view)

        if (savedInstanceState != null) {
            count = savedInstanceState.getInt("count")

            savedInstanceState.getString("name")?.let { name ->
                countTextView.text = name
            }

            countTextView.text = "$count"
        }

        addButton.setOnClickListener {
            count++
            countTextView.text = "$count"
        }
    }

    // onDestroy 가기 전에 호출
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//
//        outState.putInt("count", count)
//    }

    // 다시 재생성 될 때 onCreate 다음에 호출
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//
//        println("onRestoreInstanceState")
//
//        count = savedInstanceState.getInt("count")
//
//        val countTextView : TextView = findViewById(R.id.count_text_view)
//        countTextView.text = "$count"
//    }
}