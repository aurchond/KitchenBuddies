package com.smacm.fydp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_new_meal_session.*

class NewMealSessionFragment : Fragment() {

    private var recipeList = ArrayList<Recipe>()

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
        setRecyclerView()
    }


    // setup the recycler view
    private fun setRecyclerView() {
        val recipeAdapter = RecipeAdapter(recipeList)
        recycler_view.adapter = recipeAdapter
        recycler_view.setHasFixedSize(true)
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
    }
}