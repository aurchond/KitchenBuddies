package org.utilities.database.graph;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.recipe_processing.Recipe;

import java.util.HashMap;
import java.util.Map;

public class RecipeHelper {

    private static HashMap<String, Long> recipeNameToID;

    /* TODO:
    - Add Step to Recipe (or should we just add all the steps in one session?)
    - Update Recipe (add, delete, or modify properties)
    - Add measurement scales i.e. cups, tablespoons, etc.

    New TODO add the recipe id to the hashmap everytime a recipe is created/saved to the db
    - Save Recipe
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
        // TODO: Change to relational database
        return recipeNameToID.containsKey(recipeName);
    }

    public static void addToRecipeNameToID(String recipeName, Long recipeID){
        // TODO: Change to relational database
        recipeNameToID.put(recipeName, recipeID);
    }

    public static Recipe getRecipeFromDatabase(String recipeName){
        /**
         * two helper function getHeadNode(the head step), getAllNodes(iterable step list)
         * loop through all nodes and add to hashmap<id,step>
         */

        Session s = createSession();
        Long recipeId = recipeNameToID.get(recipeName);

        Step headNode = getHeadNode(s,recipeId).iterator().next();
        Iterable<Step> steps = copyRecipe(s, recipeId);

        HashMap<Integer, Step> stepMap = new HashMap<>();
        for(Step step: steps){
            stepMap.put(step.getStepID(), step);
        }

        return new Recipe(stepMap, headNode);
    }

    public static Session createSession() {
        String uri = "neo4j+s://db42e3f1.databases.neo4j.io";
        Configuration configuration = new Configuration.Builder()
                .uri(uri)
                .credentials("neo4j", "9hCaQ7nmAyf5AAkUwjrk5lY8ejC61PYa2-4-zLBc6hg")
                .build();
        SessionFactory sessionFactory = new SessionFactory(configuration, "org.utilities.database.graph");
        final Session session = sessionFactory.openSession();
        return session;
    }

    public static void saveRecipe(Recipe recipe){
        Long recipeID = Long.valueOf(0);//generate a recipe ID
        recipeNameToID.put(recipe.getRecipeName(), recipeID);
        /**Save recipe to db
         *  - make all step nodes have recipe id
         *  - create official node id
         *  - save each node with connections to the db
         */

    }
}