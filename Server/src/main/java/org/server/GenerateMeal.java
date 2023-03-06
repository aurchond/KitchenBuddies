package org.server;

import org.optimization.Meal;
import org.optimization.Resource;
import org.optimization.User;
import org.recipe_processing.Recipe;
import org.utilities.database.relational.MySqlConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.output_processing.OutputJson.userStepsToJson;
import static org.utilities.database.graph.RecipeHelper.getRecipeFromDatabase;
import static org.output_processing.OutputJson.userStepsToJsonOld;


public class GenerateMeal {
    public static void GenerateMeal() {}

    public static List<MealSessionUsersSteps> SetupMealSteps(KitchenConstraint kitchenConstraints, List<Long> recipeIDs, List<String> includedFriends) {
        //

        /**
         * Meal Scheduling - Combine recipes into optimized workflow
         * createMeal(List<List<Step>> recipes, List<User> buddies)
         */

        List<Recipe> recipes = new ArrayList<Recipe>();

        for (Long rId : recipeIDs) {
            Recipe r = getRecipeFromDatabase(rId);
            // r.setResourcesForRecipe(MySqlConnection.getRecipeResourcesFromId(rId));
            recipes.add(r);
        }

        Meal m = new Meal();
        List<User> buddies = new ArrayList<User>();
        //  Integer skillLevel = MySqlConnection.getSkillLevel(kitchenConstraints.userEmail);
        Integer skillLevel = 2;
        buddies.add(new User(kitchenConstraints.userEmail, skillLevel));
        for (String friend : includedFriends) {
            // skillLevel = MySqlConnection.getSkillLevel(friend);
            skillLevel = 2;
            buddies.add(new User(friend, skillLevel));
        }

        // TODO: Convert kitchen constraints to Resource Class
        // TODO: Add burner Resource oven*4
        HashMap<String, List<Resource>> constraints = setupConstraints(kitchenConstraints);

        m.createMeal(recipes, buddies, constraints);
        /**
         * Send off to users using the buddies listed and the result of create Meal
         */
        List<MealSessionUsersSteps> usersSteps = userStepsToJson(buddies);
        System.out.println("Finished");
        userStepsToJsonOld(buddies);
        return usersSteps;
        //return null;
    }

    private static HashMap<String, List<Resource>> setupConstraints(KitchenConstraint kitchenConstraints) {
        // TODO: Check if any constraints are 0 when they are needed for the recipe
        //  If this condition is true, throw an error and don't optimize
        HashMap<String, List<Resource>> constraints = new HashMap<String, List<Resource>>();
        List<Resource> pots = new ArrayList<Resource>();
        for (int i = 0; i<kitchenConstraints.pot; i++) {
            Resource pot = new Resource("pot", i);
            pots.add(pot);
        }
        constraints.put("pot", pots);

        List<Resource> pans = new ArrayList<Resource>();
        for (int i = 0; i<kitchenConstraints.pan; i++) {
            Resource pan = new Resource("pan", i);
            pans.add(pan);
        }
        constraints.put("pan", pans);

        List<Resource> bowls = new ArrayList<Resource>();
        for (int i = 0; i<kitchenConstraints.bowl; i++) {
            Resource bowl = new Resource("bowl", i);
            bowls.add(bowl);
        }
        constraints.put("bowl", bowls);

        List<Resource> cuttingBoards = new ArrayList<Resource>();
        for (int i = 0; i<kitchenConstraints.cuttingBoard; i++) {
            Resource cuttingBoard = new Resource("cutting board", i);
            cuttingBoards.add(cuttingBoard);
        }
        constraints.put("cutting board", cuttingBoards);

        List<Resource> ovens = new ArrayList<Resource>();
        for (int i = 0; i<kitchenConstraints.oven; i++) {
            Resource oven = new Resource("oven", i);
            ovens.add(oven);
        }
        constraints.put("oven", ovens);

        List<Resource> burners = new ArrayList<Resource>();
        Integer numBurners = 4;
        for (int i = 0; i<kitchenConstraints.oven*numBurners; i++) {
            Resource burner = new Resource("burner", i);
            burners.add(burner);
        }
        constraints.put("burner", burners);

        return constraints;
    }
}
