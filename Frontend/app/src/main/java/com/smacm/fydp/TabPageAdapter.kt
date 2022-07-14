package com.smacm.fydp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabPageAdapter(activity: FragmentActivity, private val tabCount: Int) : FragmentStateAdapter(activity)
{
    override fun getItemCount(): Int = tabCount //  equivalent to just return tabCount in this function

    override fun createFragment(position: Int): Fragment {
        return when (position)
        {
            0 -> HomeFragment()
            1 -> NewMealSessionFragment()
            2 -> PastRecipesFragment()
            else -> HomeFragment()
        }
    }
}