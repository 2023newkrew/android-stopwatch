package com.survivalcoding.stopwatch

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class TargetFragment : Fragment(R.layout.fragment_target) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = "바꿔"
    }
}