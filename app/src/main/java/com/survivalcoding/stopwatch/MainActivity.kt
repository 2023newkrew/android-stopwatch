package com.survivalcoding.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val addButton = findViewById<FloatingActionButton>(R.id.add_button)
        val countTextView : TextView = findViewById(R.id.count_text_view)

        countTextView.text = "${viewModel.count}"

        addButton.setOnClickListener {
            viewModel.count++
            countTextView.text = "${viewModel.count}"
        }
    }
}