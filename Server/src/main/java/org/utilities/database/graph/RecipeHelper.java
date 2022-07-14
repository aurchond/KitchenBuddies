package org.utilities.database.graph;

import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.session.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecipeHelper {

    /* TODO:
    - Add Step to Recipe (or should we just add all the steps in one session?)
    - Update Recipe (add, delete, or modify properties)
    - Add measurement scales i.e. cups, tablespoons, etc.
     */

    //search for all nodes with a specific recipe ID and delete them
    public static void deleteRecipe (Session s, Long recipeID) {
        Filter filter = new Filter ("recipeID", ComparisonOperator.EQUALS, recipeID);

        Iterable<Step> recipe = s.loadAll(Step.class, filter);
        for (Step t : recipe) {
            s.delete(t);
        }
    }

    //search for all nodes with a specific recipe ID and return the sub-graph
    public static Iterable<Step> copyRecipe(Session s, Long recipeID) {
        Filter filter = new Filter ("recipeID", ComparisonOperator.EQUALS, recipeID);

        Iterable<Step> recipe = s.loadAll(Step.class, filter);
        return recipe;
    }

    //delete step from recipe
    public static void removeStep(Session s, Double nodeID) {
        Step targetStep = s.load(Step.class, nodeID);
        s.delete(targetStep);
    }

    //get last step in a recipe
    //size of the iterable should be 1
    public static Iterable<Step> getHeadNode(Session s, Long recipeID) {
        //create a map to hold the parameter
        Map<String, Object> params = new HashMap<>(1);
        params.put ("recipe_id", recipeID);

        //  Execute query and return the other side of the married relationship
        String getHeadNodeQuery = "MATCH (c:Step {recipeID: $recipe_id}) WITH c ORDER BY c.stepID DESC LIMIT 1 RETURN c";
        return s.query(Step.class, getHeadNodeQuery, params);
    }


    // TODO: Add additional properties for a recipe
    /*
    - Add action (what is the step actually doing?) i.e. frying, baking, chopping, dicing, etc.
    - Add measurement scales i.e. cups, tablespoons, etc.
     */
    public static Boolean isRecipeInDatabase(String recipeName){
        return true;//TODO: fill in properly
    }

    public static ArrayList<Step> getRecipeFromDatabase(String recipeName){
        /**
         * two helper function getHeadNode(the head step), getAllNodes(iterable step list)
         * loop through all nodes and add to hashmap<id,step>
         */
        copyRecipe(Session s, Long recipeID)

        return new ArrayList<Step>();//TODO: fill in properly
    }

}