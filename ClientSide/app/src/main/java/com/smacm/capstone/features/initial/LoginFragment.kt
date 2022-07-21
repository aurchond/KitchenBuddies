package com.smacm.capstone.features.initial

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.smacm.capstone.R
import com.smacm.capstone.databinding.LoginFragmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.login_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = LoginFragmentBinding.bind(view)

        binding.buttonInitialGoToNext.setOnClickListener {
            findNavController().navigate(R.id.initial_to_root)
        }
    }
}