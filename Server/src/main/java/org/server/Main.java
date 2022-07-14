package org.server;

import org.optimization.Meal;
import org.optimization.User;
import org.recipe_processing.Recipe;
import org.utilities.database.graph.Step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.recipe_processing.Main.createRecipe;
import static org.utilities.database.graph.RecipeHelper.*;

public class Main {
    public static void main(String[] args) {
        List<String> recipeTitles = new ArrayList<String>(); //probs get recipe titles through arguments/twilio
        List<Recipe> recipes = new ArrayList<Recipe>();

        for (String recipeName : recipeTitles) {
            if (isRecipeInDatabase(recipeName)) {
                recipes.add(getRecipeFromDatabase(recipeName));
            } else {
                //Input processing - parse out the steps from the text - this should output a list of steps
                //TODO: get file that was passed in and parse it - look at java json lib to help with this
                // - make sure to create the below hashmaps needed for createRecipe

                //Recipe Processing - Dependency creation + Saves Steps to DB
                //TODO: convert nulls to actual values
                List<Step> Steps = null;
                HashMap<String, List<Integer>> ingredients = null;//<ingredient, List<StepId>>
                HashMap<String, List<Integer>> toolsRequired = null;//<tool, List<StepId>>
                HashMap<String, List<Integer>> holdingResource_Id = null;//<holdingResource, List<StepId>>

                Recipe recipe = createRecipe(Steps, ingredients, toolsRequired, holdingResource_Id);// String will be formatted as "holdingResource_holdingId"
                saveRecipe(recipe);
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
