package com.smacm.fydp

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class ConvertedFromJava(fragmentManager: FragmentManager, lifecycle: Lifecycle, context: Context) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragments: ArrayList<Fragment>
    private val context: Context
    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    init {
        this.context = context
        fragments = ArrayList()
        fragments.add(HomeFragment())
        fragments.add(NewMealSessionFragment())
        fragments.add(PastRecipesFragment())
    }

    override fun getItemId(position: Int): Long {
        val fragment = fragments[position]
        return getIDForFragment(fragment)
    }

    override fun containsItem(itemId: Long): Boolean {
        for (fragment in fragments) {
            if (getIDForFragment(fragment) == itemId) return true
        }
        return false
    }

    private fun getIDForFragment(fragment: Fragment): Long {
        // Insert code that generates a unique ID value for each Fragment here.
        return 5
    }

    fun replaceFragment(index: Int, fragment: Fragment?) {
        fragments[index] = fragment!!
        notifyDataSetChanged()
    }
}