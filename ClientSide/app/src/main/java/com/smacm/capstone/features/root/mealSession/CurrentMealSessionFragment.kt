package com.smacm.capstone.features.root.second

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.smacm.capstone.R
import com.smacm.capstone.databinding.FragmentCurrentMealSessionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrentMealSessionFragment : Fragment(R.layout.fragment_current_meal_session) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCurrentMealSessionBinding.bind(view)

        binding.buttonFirstFragment2GoBack.setOnClickListener {
            NavHostFragment.findNavController(requireParentFragment()).popBackStack()
        }

        (requireActivity() as OnBackPressedDispatcherOwner).onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            NavHostFragment.findNavController(requireParentFragment()).popBackStack()
        }
    }
}