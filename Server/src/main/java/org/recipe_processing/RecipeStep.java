package org.recipe_processing;

import java.util.List;

public class RecipeStep {
    /*
    This class represents a Step in a Recipe and contains the following

    Integer - id - the step number from the original recipe
    List of Sting - ingredients - list of ingredients needed in this step
    List of float - quantity - amount for each ingredient in list above
    Boolean - isPrepStep - True if this step involved preparation instead of cooking
    List of Strings - toolsRequired - list of additional told to help with the step (eg knife)
    String - holdingResource - the resource that contains the ingredients in this step (eg. cutting board, bowl, pan etc)
    Integer - holdingId - keeps track of the various holding resources (ie, bowl1 vs bowl2)
    Float - stepTime - the time it takes to complete the step in minutes TODO: need to check if we have access to a time class
    Float - recipeTimeLeft - the time it takes to complete all the steps before this one in minutes
    Integer - recipeId - identifies which recipe it is a part of (TODO: we will have to store in a db which integers match with which recipe titles)
    String - instructions - The text instructions for this step

    TODO: does the step need to know there is a time dependancy
    # TODO: double check this(how do we keep the same element between steps) - For appliance such as stove we will keep a count of which elements are free and therefore it is part of toolsRequired?
     */
    Integer id;
    List<String> ingredients;
    List<Float> quantity;
    Boolean isPrepStep;
    List<String> toolsRequired;
    String holdingResource;
    Integer holdingId;
    Float stepTime;
    Float recipeTimeLeft;
    Integer recipeId;
    String instructions;

    public RecipeStep(
            Integer id,
            List<String> ingredients,
            List<Float> quantity,
            Boolean isPrepStep,
            List<String> toolsRequired,
            String holdingResource,
            Integer holdingId,
            Float stepTime,
            Float recipeTimeLeft,
            Integer recipeId,
            String instructions
    ) {
        this.id = id;
        this.ingredients = ingredients;
        this.quantity = quantity;
        this.isPrepStep = isPrepStep;
        this.toolsRequired = toolsRequired;
        this.holdingResource = holdingResource;
        this.holdingId = holdingId;
        this.stepTime = stepTime;
        this.recipeTimeLeft = recipeTimeLeft;
        this.recipeId = recipeId;
        this.instructions = instructions;
    }

}
