package org.server;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MealSessionUsersSteps {
    @JsonProperty("userEmail")
    String userEmail;  

    @JsonProperty("notes")
    List<String> notes;   

    @JsonProperty("recipeSteps")
    List<RecipeStep> recipeSteps;

    public MealSessionUsersSteps(String userEmail, List<String> notes, List<RecipeStep> recipeSteps) {
        this.userEmail = userEmail;
        this.notes = notes;
        this.recipeSteps = recipeSteps;
    }
}
