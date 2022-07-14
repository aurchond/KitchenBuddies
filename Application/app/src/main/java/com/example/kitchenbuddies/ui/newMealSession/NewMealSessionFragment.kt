package com.example.kitchenbuddies.ui.newMealSession

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kitchenbuddies.databinding.FragmentNewMealSessionBinding
import com.example.kitchenbuddies.ui.helper.RecipeAdapter
import kotlinx.android.synthetic.main.fragment_new_meal_session.*
import layout.Recipe

class NewMealSessionFragment : Fragment() {

    private var _binding: FragmentNewMealSessionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var recipeList = ArrayList<Recipe>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(NewMealSessionViewModel::class.java)

        _binding = FragmentNewMealSessionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.tvNewMealSession
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        setRecyclerView()
    }

    // setup the recycler view
    private fun setRecyclerView() {
        val medicationsAdapter = RecipeAdapter(recipeList)
        recycler_view.adapter = medicationsAdapter
        recycler_view.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // initialize the recycler view with (temporary) mock data corresponding to mock data in calendar
    private fun initData() {
        recipeList.add(Recipe(
            "Norvasc",
            "40 mg",
            "Refill required---------- DO OTHIS and don't look back on it yuh",
            "Directions: Take daily at 9:00am (on empty stomach)."
        ))
    }
}