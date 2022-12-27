package com.survivalcoding.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addButton = findViewById<FloatingActionButton>(R.id.add_button)
        val countTextView : TextView = findViewById(R.id.count_text_view)

        viewModel.countLiveData.observe(this) { count ->
            countTextView.text = "$count"
        }

        addButton.setOnClickListener {
            viewModel.increase()
        }
    }
}