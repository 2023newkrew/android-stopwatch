package com.survivalcoding.stopwatch

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val someInt = requireArguments().getInt("init")
        val name = requireArguments().getString("name2") ?: "없음"
        println(name)

        view.findViewById<FloatingActionButton>(R.id.add_button).setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<TargetFragment>(R.id.container)
                addToBackStack(null)
            }
        }
    }

}