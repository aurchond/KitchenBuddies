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

public class Main {
    public static void main(String[] args) {
        GenerateMeal gm = new GenerateMeal();
        gm.SetupMealSteps();
    }
}
