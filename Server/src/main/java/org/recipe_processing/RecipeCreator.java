package org.recipe_processing;

import org.input_processing.RecipeStep;

import java.util.HashMap;
import java.util.List;

public class RecipeCreator {
    /**
    Need HashMaps for the following to figure out dependencies between steps:/
     VALUES IN HASHMAP ARE A LIST OF STEP IDS
    List<String> ingredients,
    List<String> toolsRequired,
    String holdingResource,
    Integer holdingId,

     We need to take into account time dependencies
     //HashMap <id, [id, time]>
     */
    public RecipeCreator() {
    }
    public Recipe createRecipe(
            List<RecipeStep> Steps,
            HashMap<String, List<Integer>> ingredients,
            HashMap<String, List<Integer>> toolsRequired,
            HashMap<String, List<Integer>> holdingResource_Id // String will be formatted as "holdingResource_holdingId"

            ){
        /**
         * 1. Step Creation - This should be done in Input Processing since the NLP should put it in the Data Structure
         *                      we have made for a Step
         * 2. Look for all the dependencies in the steps - This can be done efficiently if we do it alongside step creation
         * 3. Connect those steps - THIS APPEARS TO BE THE ONLY THING WE NEED TO DO IN RECIPE CREATION
         * 4. Save Recipe into DB before passing it to optimization
         */

        /**
         *  Iterate through keys of each hashmap and create dependencies (between ingredients in order of step id)
         *      - This doing the resource dependencies
         *
         *      toolsRequired
         *      ingredients
         *      HoldingResource
         *
         *  Time dependencies should be done after so they overwrite resource dependencies
         *      - will depend on how we get this info
         *
         *
         *  TODO: how to let a dependency replace another dependency
         */


        /**
         * Save Recipe to DB - TODO: Check with Shadi where the function for saving a recipe will be
         */
        return new Recipe(); //TODO: Replace with proper recipe
    }
}
