package org.server;

import org.optimization.Meal;
import org.optimization.User;
import org.recipe_processing.Recipe;
import org.utilities.database.graph.Step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.input_processing.Main.parseJson;
import static org.output_processing.Main.userStepsToJson;
import static org.recipe_processing.RecipeCreator.createRecipe;
import static org.utilities.database.graph.RecipeHelper.*;


public class GenerateMeal {
    public static void SetupMealSteps() {
        List<String> recipeTitles = new ArrayList<String>(); //probs get recipe titles through arguments/twilio
        recipeTitles.add("rotini");
        recipeTitles.add("fried_rice");
        recipeTitles.add("fried_veggies");
        List<Recipe> recipes = new ArrayList<Recipe>();
        Long lastRecipeID = Long.valueOf(0);

        for (String recipeName : recipeTitles) {
            if (isRecipeInDatabase(recipeName)) {
                recipes.add(getRecipeFromDatabase(recipeName));
            } else {
                /**
                 * Input processing - parse out the steps from the text - this should output a list of steps
                 */
                List<Step> Steps = new ArrayList<Step>();
                HashMap<String, List<Integer>> ingredients = new HashMap<String, List<Integer>>();//<ingredient, List<StepId>>
                HashMap<String, List<Integer>> resourcesRequired = new HashMap<String, List<Integer>>();//<tool, List<StepId>>
                HashMap<String, List<Integer>> holdingResource_Id = new HashMap<String, List<Integer>>();//<holdingResource, List<StepId>>
                parseJson(String.format("res/input/%s.json", recipeName), Steps, ingredients, resourcesRequired, holdingResource_Id);
                // make sure hashmaps have the stepIDs in order of smallest to largest

                /**
                 * Recipe Processing - Dependency creation + Saves Steps to DB
                 */
                
                Recipe recipe = createRecipe(Steps, ingredients, resourcesRequired, holdingResource_Id, lastRecipeID++);// String will be formatted as "holdingResource_holdingId"
                recipe.setRecipeName(recipeName);
                //access graph DB so we can save the recipe
                //saveRecipe(recipe, recipeName);
                recipes.add(recipe);


            }
        }
        /**
         * Meal Scheduling - Combine recipes into optimized workflow
         * createMeal(List<List<Step>> recipes, List<User> buddies)
         */
        Meal m = new Meal();
        List<User> buddies = new ArrayList<User>();
        buddies.add(new User("Marley"));
        buddies.add(new User("Aurchon"));
        buddies.add(new User("Shadi"));
        m.createMeal(recipes, buddies);
        /**
         * Send off to users using the buddies listed and the result of create Meal
         */
        userStepsToJson(buddies);
        System.out.println("Finished");
    }
}