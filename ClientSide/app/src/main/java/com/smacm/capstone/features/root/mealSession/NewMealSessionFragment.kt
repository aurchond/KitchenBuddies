package com.smacm.capstone.features.root.second

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.smacm.capstone.R
import com.smacm.capstone.databinding.FragmentNewMealSessionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewMealSessionFragment : Fragment(R.layout.fragment_new_meal_session) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNewMealSessionBinding.bind(view)

        binding.buttonFirstFragment1GoTo2.setOnClickListener {
            NavHostFragment.findNavController(requireParentFragment()).navigate(R.id.first1_to_first2)
        }
    }
}