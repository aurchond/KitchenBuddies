package org.optimization;

import org.junit.jupiter.api.Test;
import org.recipe_processing.Recipe;
import org.utilities.database.graph.RecipeHelper;
import org.output_processing.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class test_optimization {
    @Test
    public void testAdd() {

        assertEquals(42, Integer.sum(19, 23));
    }

    @Test
    public void testGetUserConstraints() {

        assertEquals(42, Integer.sum(19, 23));
    }

    @Test
    public void testOptimize() {
        // Setup
        List<Recipe> recipes = new ArrayList<Recipe>();

        List<String> recipeNames = Arrays.asList("rotini", "fried_rice", "salmon");

        for (String rName: recipeNames) {
            recipes.add(RecipeHelper.getRecipeFromDatabase(rName));
        }

        List<User> buddies = new ArrayList<User>();
        buddies.add(new User("Marley"));
        buddies.add(new User("Aurchon"));
        buddies.add(new User("Shadi"));

        Meal m = new Meal();
        m.createMeal(recipes, buddies);

        for (User b: buddies) {
            b.printStepList();
        }

        System.out.println("Finished Test!");

        Main.userStepsToJson(buddies);

    }
}
