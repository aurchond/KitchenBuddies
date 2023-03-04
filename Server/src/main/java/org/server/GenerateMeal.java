package org.server;

import org.optimization.Meal;
import org.optimization.User;
import org.recipe_processing.Recipe;
import org.utilities.database.relational.MySqlConnection;

import java.util.ArrayList;
import java.util.List;

import static org.output_processing.OutputJson.userStepsToJson;
import static org.utilities.database.graph.RecipeHelper.getRecipeFromDatabase;


public class GenerateMeal {

    public static List<MealSessionUsersSteps> SetupMealSteps(KitchenConstraint kitchenConstraints, List<Long> recipeIDs, List<String> includedFriends) {
        //

        /**
         * Meal Scheduling - Combine recipes into optimized workflow
         * createMeal(List<List<Step>> recipes, List<User> buddies)
         */

        List<Recipe> recipes = new ArrayList<Recipe>();
        for (Long rId : recipeIDs) {
            Recipe r = getRecipeFromDatabase(rId);
            r.setRecipeName(MySqlConnection.getRecipeNameFromId(rId));
            recipes.add(r);
        }

        Meal m = new Meal();
        List<User> buddies = new ArrayList<User>();
        Integer skillLevel = MySqlConnection.getSkillLevel(kitchenConstraints.userEmail);
        buddies.add(new User(kitchenConstraints.userEmail, skillLevel));
        for (String friend : includedFriends) {
            skillLevel = MySqlConnection.getSkillLevel(friend);
            buddies.add(new User(friend, skillLevel));
        }
        m.createMeal(recipes, buddies);
        /**
         * Send off to users using the buddies listed and the result of create Meal
         */
        List<MealSessionUsersSteps> usersSteps = userStepsToJson(buddies);
        System.out.println("Finished");
        return usersSteps;
    }
}