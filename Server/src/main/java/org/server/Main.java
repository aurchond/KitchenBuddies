package org.server;

import org.optimization.Meal;
import org.optimization.User;
import org.utilities.database.graph.Step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.recipe_processing.Main.createRecipe;
import static org.utilities.database.graph.Main.getRecipeFromDatabase;
import static org.utilities.database.graph.Main.isRecipeInDatabase;

public class Main {
    public static void main(String[] args) {
        List<String> recipeTitles = new ArrayList<String>(); //probs get recipe titles through arguments/twilio
        List<List<Step>> recipes = new ArrayList<List<Step>>();
        /**passed in X number of recipe names
         */
         for(String recipeName: recipeTitles){
             if (isRecipeInDatabase(recipeName)) {
                 recipes.add(getRecipeFromDatabase(recipeName));
                 //if so retrieve it and add it to recipes list
             }else {
                 //Input processing - parse out the steps from the text - this should output a list of steps

                 //          Recipe Processing - Dependency creation + Saves Steps to DB
                 //TODO: convert nulls to actual values
                 List<Step> Steps = null;
                 HashMap<String, List<Integer>> ingredients = null;
                 HashMap<String, List<Integer>> toolsRequired = null;
                 HashMap<String, List<Integer>> holdingResource_Id = null;

                List<Step> recipe = createRecipe( Steps, ingredients, toolsRequired, holdingResource_Id );// String will be formatted as "holdingResource_holdingId"
                recipes.add(recipe);

             }
          }
        /**
         * Meal Scheduling - Combine recipes into optimized workflow
         * createMeal(List<List<Step>> recipes, List<User> buddies)
         */
         Meal m = new Meal();
         List<User> buddies = null;
         m.createMeal(recipes, buddies);
         /**
         * Send off to users using the buddies listed and the result of create Meal
        */
        //

        //

        //
    }

}
