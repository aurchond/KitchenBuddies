package com.smacm.capstone.features.root

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import com.smacm.capstone.R
import com.smacm.capstone.core.navigation.FragmentStackHostFragment
import com.smacm.capstone.databinding.RootFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootFragment : Fragment(R.layout.root_fragment) {
    private val binding by viewBinding(RootFragmentBinding::bind)

    private lateinit var newMealSessionFragment: FragmentStackHostFragment
    private lateinit var homeFragment: FragmentStackHostFragment
    private lateinit var pastRecipesFragment: FragmentStackHostFragment

    private val fragments: Array<Fragment> get() = arrayOf(newMealSessionFragment, homeFragment, pastRecipesFragment)
    private var selectedIndex = 0

    private val tabs: Array<TextView>
        get() = binding.run {
            arrayOf(buttonFirstTab, buttonSecondTab, buttonThirdTab)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val newMealSessionFragment = FragmentStackHostFragment.newInstance(R.navigation.navigation_root_first).also { this.newMealSessionFragment = it }
            val homeFragment = FragmentStackHostFragment.newInstance(R.navigation.navigation_root_second).also { this.homeFragment = it }
            val pastRecipesFragment = FragmentStackHostFragment.newInstance(R.navigation.navigation_root_third).also { this.pastRecipesFragment = it }

            childFragmentManager.beginTransaction()
                .add(R.id.containerBottomNavContent, newMealSessionFragment, "newMealSessionFragment")
                .add(R.id.containerBottomNavContent, homeFragment, "homeFragment")
                .add(R.id.containerBottomNavContent, pastRecipesFragment, "pastRecipesFragment")
                .selectFragment(selectedIndex)
                .commit()
        } else {
            selectedIndex = savedInstanceState.getInt("selectedIndex", 0)

            newMealSessionFragment = childFragmentManager.findFragmentByTag("newMealSessionFragment") as FragmentStackHostFragment
            homeFragment = childFragmentManager.findFragmentByTag("homeFragment") as FragmentStackHostFragment
            pastRecipesFragment = childFragmentManager.findFragmentByTag("pastRecipesFragment") as FragmentStackHostFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabs.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                selectFragment(index)
            }
        }

        setupTabSelectedState(selectedIndex)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedIndex", selectedIndex)
    }

    private fun FragmentTransaction.selectFragment(selectedIndex: Int): FragmentTransaction {
        fragments.forEachIndexed { index, fragment ->
            if (index == selectedIndex) {
                attach(fragment)
            } else {
                detach(fragment)
            }
        }

        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        return this
    }

    private fun setupTabSelectedState(selectedIndex: Int) {
        tabs.forEachIndexed { index, textView ->
            textView.setTextColor(when {
                index == selectedIndex -> ContextCompat.getColor(requireContext(), R.color.tab_selected)
                else -> ContextCompat.getColor(requireContext(), R.color.tab_unselected)
            })
        }
    }

    private fun selectFragment(indexToSelect: Int) {
        this.selectedIndex = indexToSelect

        setupTabSelectedState(indexToSelect)

        childFragmentManager.beginTransaction()
            .selectFragment(indexToSelect)
            .commit()
    }
}