package com.example.kitchenbuddies


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.simple_expander.view.*
import layout.Recipe

class RecipeAdapter(val recipeList: List<Recipe>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>(){

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder (
            LayoutInflater.from(parent.context).inflate( // takes 4 arguments, all optional: resources, parser, resources, parser
                R.layout.simple_expander, // R is to access resources, layout is a resource
                parent,
                false // don't wanna attach this view to the root layout, set to false
            )
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currRecipe: Recipe = recipeList[position]

        // below code makes it so that u don't need to prepend with holder.itemView each time you make a change
        holder.itemView.apply {
            tv_title.text = currRecipe.name
            tv_prop1.text = currRecipe.cook_time
            tv_prop2.text = currRecipe.difficulty
            tv_prop3.text = currRecipe.previous_meal_session

            val isExpandable : Boolean = currRecipe.expandable
            rl_expandable_layout.visibility = if (isExpandable) View.GONE else View.VISIBLE

            linear_layout.setOnClickListener {
                // toggle the box being expandable
                currRecipe.expandable = !currRecipe.expandable
                notifyItemChanged(position)
            }
        }

    }

    override fun getItemCount(): Int {
        return recipeList.size
    }
}