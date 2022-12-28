package com.survivalcoding.stopwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.survivalcoding.stopwatch.databinding.FragmentMainBinding

class MainFragment(val init: Int) : Fragment() {

    private var _binding: FragmentMainBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        view.findViewById<TextView>(R.id.count_text_view).text = "$init"
        binding.countTextView.text = "$init"

        view.findViewById<FloatingActionButton>(R.id.add_button).setOnClickListener {


//            parentFragmentManager.commit {
//                setReorderingAllowed(true)
//                replace<TargetFragment>(R.id.container)
//                addToBackStack(null)
//            }
        }

        viewModel.countLiveData.observe(viewLifecycleOwner) { count ->
            binding.countTextView.text = "$count"
        }

        binding.addButton.setOnClickListener {
            viewModel.increase()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}