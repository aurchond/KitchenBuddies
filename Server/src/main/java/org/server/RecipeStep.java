package org.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeStep {
    @JsonProperty("number")
    String number;

    @JsonProperty("instructions")
    String instructions;
    
    @JsonProperty("ingredientList")
    List<String> ingredientList;
    
    // @JsonProperty("ingredientQuantity")
    // List<Float> ingredientQuantity;
    
    @JsonProperty("dependencyItem")
    String dependencyItem;
    
    @JsonProperty("nextUserEmail")
    String nextUserEmail;
    Integer timeOfStep;

    public Integer getTimeOfStep() {
        return timeOfStep;
    }

    public void setTimeOfStep(Integer timeOfStep) {
        this.timeOfStep = timeOfStep;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
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

    // public List<Float> getIngredientQuantity() {
    //     return ingredientQuantity;
    // }

    // public void setIngredientQuantity(List<Float> ingredientQuantity) {
    //     this.ingredientQuantity = ingredientQuantity;
    // }

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
