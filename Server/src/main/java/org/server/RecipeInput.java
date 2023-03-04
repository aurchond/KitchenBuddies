package org.server;

import java.util.List;

public class RecipeInput {
    Long recipeID;
    String recipeName;
    List<String> ingredientList;
    Integer totalTimeMinutes;
    String lastDateMade;
    String userEmail;
    List<String> instructionList;

    public Long getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(Long recipeID) {
        this.recipeID = recipeID;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public List<String> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<String> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public Integer getTotalTimeMinutes() {
        return totalTimeMinutes;
    }

    public void setTotalTimeMinutes(Integer totalTimeMinutes) {
        this.totalTimeMinutes = totalTimeMinutes;
    }

    public String getLastDateMade() {
        return lastDateMade;
    }

    public void setLastDateMade(String lastDateMade) {
        this.lastDateMade = lastDateMade;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<String> getInstructionList() {
        return instructionList;
    }

    public void setInstructionList(List<String> instructionList) {
        this.instructionList = instructionList;
    }
}
