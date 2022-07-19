package org.recipe_processing;

import org.utilities.database.graph.Connection;
import org.utilities.database.graph.Step;

import java.util.*;

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
    public static Recipe createRecipe(
            List<Step> Steps,
            HashMap<String, List<Integer>> ingredients,
            HashMap<String, List<Integer>> resourcesRequired,
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
         *      resourcesRequired
         *      ingredients
         *      HoldingResource
         *
         *  Time dependencies should be done after so they overwrite resource dependencies
         *      - will depend on how we get this info
         *
         *
         *  TODO: how to let a dependency replace another dependency
         */

        //TODO: Check if we want to set the recipe ID in here
        Recipe recipe = new Recipe();
        HashMap<Integer, Step> steps = new HashMap<Integer, Step>();
        for (Step step : Steps) {
            steps.put(step.getStepID(), step);
        }
        recipe.setSteps(steps);
        for (List<Integer> stepIds : ingredients.values()) {
            createConnections(steps, stepIds);
        }
        // if you go in order for lowest to highest step, each step should only have one connection, than flip the connections at the end
        for (List<Integer> stepIds : resourcesRequired.values()) {
            createConnections(steps, stepIds);
        }
        for (List<Integer> stepIds : holdingResource_Id.values()) {
            createConnections(steps, stepIds);
        }

        //collapse dependancies
        /**
        Iterate through each step {
            If (current step has > 1 connection) {
                - Check lowest step connection it has
                - Go through all connections from 'lowest' to 'highest' step {
                    if you can make a 'path' from the connected step -> current step that !directConnection  --- use DFS{
                        remove connection from connected step to current step
                    }
                }
            }
         }


         **/
        for (Step step : Steps) {
            if (step.hasMultipleConnection()){
                List<Connection> connections = new ArrayList<>();
                connections.addAll(step.getConnections());
                Collections.sort(connections, new Comparator<Connection>() {
                    public int compare(Connection o1, Connection o2) {
                        // compare two instance of `Score` and return `int` as result.
                        return (o2.getEndNode().getStepID()).compareTo(o1.getEndNode().getStepID());
                    }
                });
                for(Connection c: connections){
                    if(isFindsPath(c.getStartNode(), c.getEndNode())){
                        step.deleteConnection(c.getEndNode());
                    }
                }
            }
        }


        //TODO: add nodes to recipe

        //assign the time left for each step

        return recipe; //TODO: Replace with proper recipe
    }

    private boolean isFindsPath(Step startNode, Step endNode) {
        //TODO implement DFS if you find a path than you return true
    }

    private void createConnections(HashMap<Integer, Step> steps, List<Integer> stepIds) {
        for (Integer i = 0; i < stepIds.size() - 1 ; i++) {
            Integer stepId1 = stepIds.get(i);
            Integer stepId2 = stepIds.get(i+1);
            if(!steps.get(stepId1).hasResourceConnection(steps.get(stepId2))){
                steps.get(stepId1).addConnection(steps.get(stepId2));
            }
        }
    }
}
