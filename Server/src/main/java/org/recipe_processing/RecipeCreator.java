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
            List<Step> steps,//TODO: ENFORCE ASSUMPTION THAT STEPS IS IN ORDER OF STEPID
            HashMap<String, List<Integer>> ingredients,
            HashMap<String, List<Integer>> resourcesRequired,
            HashMap<String, List<Integer>> holdingResource_Id, // String will be formatted as "holdingResource_holdingId"
            Long recipeID
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
         */

        Recipe recipe = new Recipe();
        HashMap<Integer, Step> stepsMap = new HashMap<Integer, Step>();
        for (Step step : steps) {
            stepsMap.put(step.getStepID(), step);
        }
        recipe.setSteps(stepsMap);
        for (List<Integer> stepIds : ingredients.values()) {
            createConnections(stepsMap, stepIds);
        }
        // if you go in order for lowest to highest step, each step should only have one connection, than flip the connections at the end
        for (List<Integer> stepIds : resourcesRequired.values()) {
            createConnections(stepsMap, stepIds);
        }
        for (List<Integer> stepIds : holdingResource_Id.values()) {
            createConnections(stepsMap, stepIds);
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
        for (Step step : steps) {
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
                    if(isFindsPath(c.getStartNode(), c.getEndNode(), true)){
                        step.deleteConnection(c.getEndNode());
                    }
                }
            }
        }

        /**
         *
        TODO: how do we want to create recipe ids
         private Integer recipeID;
         */

//TODO: ENFORCE ASSUMPTION THAT LARGEST STEP IS FINAL STEP BECAUSE IT SHOULD USE EVERYTHING/COMBINE EVERYTHING
        //assign the time left for each step
        for (Step step : steps) {
            step.setRecipeID(recipeID);
            step.setNodeID(Double.valueOf((recipeID)+"."+step.getStepID()));
            step.setName(step.getNodeID().toString());
            List<Connection> connections = new ArrayList<>();
            connections.addAll(step.getConnections());
            for(Connection c: connections){
                if(c.getEndNode().getStepID() > step.getStepID()) {
                    c.getEndNode().addConnection(step);
                    step.deleteConnection(c.getEndNode());
                   // connections.remove(c);
                }
            }
            if(step.getConnections().size()==0){
                step.setTimeLeft(step.getStepTime());
            }else{
                Integer maxTimeLeft = 0;
                for(Connection c: step.getConnections()){
                    if(c.getEndNode().getTimeLeft() > maxTimeLeft) {
                        maxTimeLeft = c.getEndNode().getTimeLeft();
                    }
                }
                step.setTimeLeft(maxTimeLeft+step.getStepTime());
            }
        }
        recipe.setFinalStep(steps.get(steps.size()-1));
        recipe.setRecipeID(recipeID);
        return recipe;
    }

    private static boolean isFindsPath(Step startNode, Step endNode, Boolean depthIsOne) {
        // DFS Implementation to check if path between two nodes exist
        if (startNode.getStepID() == endNode.getStepID() ) {//&& !depthIsOne
            // Only return true if this is not a direct connection between original node and end node
            return true;
        }

        Set<Connection> connections = startNode.getConnections();
        for (Connection c: connections) {
            if (!(depthIsOne && c.getEndNode().getStepID() == endNode.getStepID()) ) {
                Boolean isPath = isFindsPath(c.getEndNode(), endNode, false);

                if (isPath) {
                    return true;
                }
            }
        }

        return false;
    }

    private static void createConnections(HashMap<Integer, Step> steps, List<Integer> stepIds) {
        for (Integer i = 0; i < stepIds.size() - 1 ; i++) {
            Integer stepId1 = stepIds.get(i);
            Integer stepId2 = stepIds.get(i+1);
            if(!steps.get(stepId1).hasResourceConnection(steps.get(stepId2))){
                steps.get(stepId1).addConnection(steps.get(stepId2));
            }
        }
    }
}
