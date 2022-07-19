package com.smacm.capstone.core.navigation

import android.os.Bundle
import androidx.annotation.NavigationRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.smacm.capstone.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentStackHostFragment : Fragment(R.layout.stack_host_fragment) {
    companion object {
        fun newInstance(@NavigationRes navigationId: Int): FragmentStackHostFragment =
            FragmentStackHostFragment().apply {
                arguments = Bundle().also { bundle ->
                    bundle.putInt("navigationId", navigationId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val childNavHostFragment = NavHostFragment.create(requireArguments().getInt("navigationId"))

            childFragmentManager.beginTransaction()
                .add(R.id.containerStackHost, childNavHostFragment, "NavHostFragment")
                .commitAllowingStateLoss()
        }
    }
}