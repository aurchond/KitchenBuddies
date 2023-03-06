package org.recipe_processing;

import org.utilities.database.graph.Step;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Recipe {
    // Metadata
    // Title
    // Overall time

    // Hashmap of all Steps <stepId, Step>
    private HashMap<Integer, Step> steps;
    // Step headNode which holds the last step
    private Step finalStep;

    private String recipeName;

    private Long recipeID;
    private Set<String> resourcesForRecipe;

    public Recipe() {
        this.steps = new HashMap<>();
        this.resourcesForRecipe = new HashSet<>();
    }

    public Recipe(HashMap<Integer, Step> steps, Step finalStep) {
        this.steps = steps;
        this.finalStep = finalStep;
        this.resourcesForRecipe = new HashSet<>();
    }

    public Recipe(HashMap<Integer, Step> steps, Step finalStep, Long recipeID) {
        this.steps = steps;
        this.finalStep = finalStep;
        this.recipeID = recipeID;
        this.resourcesForRecipe = new HashSet<>();
    }

    public Set<String> getResourcesForRecipe() {
        return resourcesForRecipe;
    }

    public void setResourcesForRecipe(Set<String> resourcesForRecipe) {
        this.resourcesForRecipe = resourcesForRecipe;
    }

    public void addResourcesForRecipe(List<String> resources, String holdingResource) {
        this.resourcesForRecipe.add(holdingResource);
        this.resourcesForRecipe.addAll(resources);
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

    public Long getRecipeID() {
        return this.recipeID;
    }

    public void setRecipeID(Long recipeID) {
        this.recipeID = recipeID;
    }
}