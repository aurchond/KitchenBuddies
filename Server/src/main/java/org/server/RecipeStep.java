package org.server;

import java.util.List;

public class RecipeStep {
    Double number;
    String instructions;
    List<String> ingredientList;
    List<Float> ingredientQuantity;
    String dependencyItem;
    String nextUserEmail;

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<String> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<String> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public List<Float> getIngredientQuantity() {
        return ingredientQuantity;
    }

    public void setIngredientQuantity(List<Float> ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }

    public String getDependencyItem() {
        return dependencyItem;
    }

    public void setDependencyItem(String dependencyItem) {
        this.dependencyItem = dependencyItem;
    }

    public String getNextUserEmail() {
        return nextUserEmail;
    }

    public void setNextUserEmail(String nextUserEmail) {
        this.nextUserEmail = nextUserEmail;
    }
}
