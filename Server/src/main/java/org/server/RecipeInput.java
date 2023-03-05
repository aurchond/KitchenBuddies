package org.server;

import java.util.List;

public class RecipeInput {
    String recipeName;
    List<String> ingredientList;
    Integer totalTime;
    String userEmail;
    List<String> instructionList;

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

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
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
