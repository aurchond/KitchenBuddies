package org.server;

import org.optimization.Meal;
import org.optimization.User;
import org.recipe_processing.Recipe;
import org.utilities.database.graph.Step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.input_processing.Main.parseJson;
import static org.recipe_processing.RecipeCreator.createRecipe;
import static org.utilities.database.graph.RecipeHelper.*;

public class Main {
    public static void main(String[] args) {
        List<String> recipeTitles = new ArrayList<String>(); //probs get recipe titles through arguments/twilio
        recipeTitles.add("test");
        List<Recipe> recipes = new ArrayList<Recipe>();

        for (String recipeName : recipeTitles) {
            if (isRecipeInDatabase(recipeName)) {
                recipes.add(getRecipeFromDatabase(recipeName));
            } else {
                //Input processing - parse out the steps from the text - this should output a list of steps
                List<Step> Steps = new ArrayList<Step>();
                HashMap<String, List<Integer>> ingredients = new HashMap<String, List<Integer>>();//<ingredient, List<StepId>>
                HashMap<String, List<Integer>> resourcesRequired = new HashMap<String, List<Integer>>();//<tool, List<StepId>>
                HashMap<String, List<Integer>> holdingResource_Id = new HashMap<String, List<Integer>>();//<holdingResource, List<StepId>>
                parseJson("res/test.json", Steps, ingredients, resourcesRequired, holdingResource_Id);
                //TODO: make sure hashmaps have the stepIDs in order of smallest to largest

                //Recipe Processing - Dependency creation + Saves Steps to DB

                Recipe recipe = createRecipe(Steps, ingredients, resourcesRequired, holdingResource_Id);// String will be formatted as "holdingResource_holdingId"
                recipe.setRecipeName(recipeName);
                addToRecipeNameToID(recipeName, recipe.getRecipeID());
                //access graph DB so we can save the recipe
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
        //Iterate through users to make steps into a list
        //

        //

        //
    }

}
