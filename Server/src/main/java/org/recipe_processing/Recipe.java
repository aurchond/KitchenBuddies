package org.recipe_processing;

import org.utilities.database.graph.Step;

import java.util.HashMap;

public class Recipe {
    /*
    TODO: we need a way to specify time and resource dependencies between steps (these might be different links)
    */
    // Metadata
    // Title
    // Overall time

    // Hashmap of all Steps <stepId, Step>
    private HashMap<Integer, Step> steps;
    // Step headNode which holds the last step
    private Step finalStep;

    private String recipeName;

    private Long recipeID;

    public Recipe() {
        this.steps = new HashMap<>();
    }

    public Recipe(HashMap<Integer, Step> steps, Step finalStep) {
        this.steps = steps;
        this.finalStep = finalStep;
    }

    public Recipe(HashMap<Integer, Step> steps, Step finalStep, Long recipeID) {
        this.steps = steps;
        this.finalStep = finalStep;
        this.recipeID = recipeID;
    }

    public HashMap<Integer, Step> getSteps() {
        return steps;
    }

    public void setSteps(HashMap<Integer, Step> steps) {
        this.steps = steps;
    }

    public void addStep(Step step) {
        this.steps.put(step.getStepID(), step);
    }

    public Step getFinalStep() {
        return finalStep;
    }

    public void setFinalStep(Step finalStep) {
        this.finalStep = finalStep;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public Long getRecipeID() { return this.recipeID;}

    public void setRecipeID( Long recipeID ) { this.recipeID = recipeID;}
}