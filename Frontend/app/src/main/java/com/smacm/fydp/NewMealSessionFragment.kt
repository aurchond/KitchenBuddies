package com.smacm.fydp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_new_meal_session.*

class NewMealSessionFragment : Fragment() {

    private var recipeList = ArrayList<Recipe>()
    private val buddyList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_meal_session, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        setSelectedRecipesRV()
        setCookingBuddiesRV()
    }


    // setup the recycler view
    private fun setSelectedRecipesRV() {
        val recipeAdapter = RecipeAdapter(recipeList)
        rv_selected_recipes.adapter = recipeAdapter
        rv_selected_recipes.setHasFixedSize(true)
    }

    private fun setCookingBuddiesRV() {
        val buddiesAdapter = BuddyAdapter(buddyList)
//        (activity as MainActivity).setLayoutManager()
        rv_cooking_buddies.setLayoutManager(
            LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        rv_cooking_buddies.adapter = buddiesAdapter
//        rv_cooking_buddies.setHasFixedSize(true)
    }

    // initialize the recycler view with (temporary) mock data corresponding to mock data in calendar
    private fun initData() {
        recipeList.add(Recipe(
            "Lasagna",
            "10 mins",
            "Hard",
            "October 22, 2021"
        ))

        recipeList.add(Recipe(
            "Lasagna",
            "10 mins",
            "Hard",
            "October 22, 2021"
        ))

        recipeList.add(Recipe(
            "Lasagna",
            "10 mins",
            "Hard",
            "October 22, 2021"
        ))


        for (i in 1..10) {
            buddyList.add("Buddy # $i")
        }
    }
}