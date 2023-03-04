package org.server;

import java.util.List;

public class MealSessionUsersSteps {
    String userEmail;
    String notes;
    List<RecipeStep> recipeSteps;

    public MealSessionUsersSteps(String userEmail, String notes, List<RecipeStep> recipeSteps) {
        this.userEmail = userEmail;
        this.notes = notes;
        this.recipeSteps = recipeSteps;
    }
}
