package org.utilities.database.graph;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        buildSampleRecipe();
        testHelperFunctions();
    }

    private static void testHelperFunctions() {
        System.out.println("Hello world!");
        String uri = "neo4j+s://db42e3f1.databases.neo4j.io";
        Configuration configuration = new Configuration.Builder()
                .uri(uri)
                .credentials("neo4j", "9hCaQ7nmAyf5AAkUwjrk5lY8ejC61PYa2-4-zLBc6hg")
                .build();
        SessionFactory sessionFactory = new SessionFactory(configuration, "org.utilities.database.graph");
        final Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Step s_test = new Step(372L, 7, false, "pan",
                0, 5, 1, "fry");
        s_test.setStepID(5);
        session.save(s_test);

       RecipeHelper.deleteRecipe(session, 372L);
       Iterable<Step> myRecipe;
       myRecipe = RecipeHelper.copyRecipe(session, 25L);
       for (Step t : myRecipe) {
           Set<Connection> mySet = t.getConnections();
           if (!mySet.isEmpty()) { //remember to have this check every time we look through a set!
               System.out.println(mySet.iterator().next().getId());
           }
           //NOTE: you can modify the properties of the nodes
           // but DO NOT save them otherwise the original recipe will be replaced
           // there is a clone method available w/ APOC (call apoc.refactor.cloneNodes)
       }

       Iterable<Step> myHead;
       myHead = RecipeHelper.getHeadNode(session, 26L);
       for (Step t : myHead) {
           System.out.println(t.getNodeID());
       }

       tx.commit();
       tx.close();
    }

    private static void buildSampleRecipe() {
        System.out.println("Hello world!");
        String uri = "neo4j+s://db42e3f1.databases.neo4j.io";
        Configuration configuration = new Configuration.Builder()
                .uri(uri)
                .credentials("neo4j", "9hCaQ7nmAyf5AAkUwjrk5lY8ejC61PYa2-4-zLBc6hg")
                .build();
        SessionFactory sessionFactory = new SessionFactory(configuration, "org.utilities.database.graph");
        final Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        // These recipes are hard coded for testing purposes
        createAsparagus(session);
        createChicken(session);

       tx.commit();
       tx.close();
    }

    //https://www.simplyrecipes.com/recipes/asparagus/
    public static void createAsparagus(Session s) {
        // Step 1
        // Boil water in saucepan
        List<String> r1_ingredients = new ArrayList<String>();
        r1_ingredients.add("salted water");

        List<Number> r1_quantity = new ArrayList<Number>();
        r1_quantity.add(0.5);

        List<String> r1_tools = new ArrayList<String>();
        Step s1 = new Step(25L, 1, true, "saucepan",
                0, 1, 10, r1_ingredients, r1_quantity, r1_tools, "boil");

        // Step 2
        // Prepare the asparagus while the water is heating. Rinse them thoroughly. Break off any tough, white bottoms and discard.
        //Cut the spears into 1- to 2-inch sections, slicing the asparagus at a slight diagonal.
        List<String> r2_ingredients = new ArrayList<String>();
        r2_ingredients.add("asparagus");
        List<Number> r2_quantity = new ArrayList<Number>();
        r2_quantity.add(1);

        List<String> r2_tools = new ArrayList<String>();
        r2_tools.add("knife");

        Step s2 = new Step(25L, 2, true, "cutting board",
                0, 5, 5, r2_ingredients, r2_quantity, r2_tools, "cut");

        // Step 3
        // Boil asparagus and lower heat
        List<String> r3_ingredients = new ArrayList<String>();
        r2_ingredients.add("asparagus");
        List<Number> r3_quantity = new ArrayList<Number>();
        r2_quantity.add(1);

        Step s3 = new Step(25L, 3, false, "saucepan",
                0, 10, 20, r2_ingredients, r2_quantity, r2_tools, "boil");

        // Step 4
        // Drain water, add ingredients
        // Toss the asparagus with olive oil, parmesan, and lemon zest while it's still hot.
        //
        //Salt and pepper to taste. Serve warm or room temperature.
        List<String> r4_ingredients = new ArrayList<String>();
        r2_ingredients.add("asparagus");
        r2_ingredients.add("olive oil");
        r2_ingredients.add("parmesan");
        r2_ingredients.add("lemon zest");
        List<Number> r4_quantity = new ArrayList<Number>();
        r2_quantity.add(1);
        r2_quantity.add(2);
        r2_quantity.add(2);
        r2_quantity.add(1);

        List<String> r4_tools = new ArrayList<String>();
        r2_tools.add("spoon");

        Step s4 = new Step(25L, 4, false, "bowl",
                0, 5, 25, r4_ingredients, r4_quantity, r4_tools, "season");

        // Step 1 -> Step 3 with 10 timeleft
        s1.addConnection(s3, 10);
        // Step 2 -> Step 3 with 5 timeleft
        s2.addConnection(s3, 5);
        // Step 3 -> Step 4 with 20 timeleft
        s3.addConnection(s4, 20);

        s.save(s1);
        s.save(s2);
        s.save(s3);

//        return s;
    }

    public static void createChicken(Session s) {
        // Step 1
        // Stir spices
        List<String> ingredients = new ArrayList<String>();
        ingredients.add("Flour");
        ingredients.add("Salt");
        ingredients.add("Pepper");
        ingredients.add("Tarragon");
        ingredients.add("Ginger");
        ingredients.add("Mustard Powder");
        ingredients.add("Thyme");
        ingredients.add("Garlic Powder");
        ingredients.add("Oregano");

        List<Number> quantity = new ArrayList<Number>();
        quantity.add(2);
        quantity.add(2);
        quantity.add(2);
        quantity.add(1);
        quantity.add(1);
        quantity.add(1);
        quantity.add(1);
        quantity.add(1);
        quantity.add(1);

        List<String> tools = new ArrayList<String>();
        tools.add("Spoon");
        Step s1 = new Step(26L, 1, true, "bowl",
                0, 5, 5, ingredients, quantity, tools, "mix");

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 2
        // Beat eggs/milk
        ingredients.add("Eggs");
        ingredients.add("Milk");

        quantity.add(1);
        quantity.add(0.25);

        tools.add("Spoon");
        Step s2 = new Step(26L, 2, true, "bowl",
                0, 5, 5, ingredients, quantity, tools, "mix");

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 3
        // Dredge Chicken
        ingredients.add("Flour mix");
        ingredients.add("Egg mix");
        ingredients.add("Chicken");

        quantity.add(1);
        quantity.add(1);
        quantity.add(8);

        Step s3 = new Step(26L, 3, true, "plate",
                0, 10, 15, ingredients, quantity, tools, "dredge");

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 4
        // Heat oven
        Step s4 = new Step(26L, 4, false, "none",
                0, 5, 1, "pre-heat");

        // Step 5
        // Heat pan
        Step s5 = new Step(26L, 5, false, "pan",
                0, 1, 5, "heat");

        // Step 6
        // Pan fry chicken
        ingredients.add("Chicken");
        quantity.add(8);
        tools.add("Spatula");
        Step s6 = new Step(26L, 6, true, "none",
                0, 5, 20, ingredients, quantity, tools, "fry");

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 7
        // Bake chicken
        ingredients.add("Chicken");
        quantity.add(8);
        tools.add("Spatula");
        Step s7 = new Step(26L, 7, false, "baking sheet",
                0, 2, 22, ingredients, quantity, tools, "bake");

        // Step 8
        // Take out food
        Step s8 = new Step(26L, 8, false, "baking sheet",
                0, 2, 50, "remove");


        s1.addConnection(s3, 5);
        s2.addConnection(s3, 5);
        s3.addConnection(s6, 15);
        s4.addConnection(s7, 5);
        s5.addConnection(s6, 5);
        s6.addConnection(s7, 20);
        s7.addConnection(s8, 50);

        s.save(s1);
        s.save(s2);
        s.save(s3);
        s.save(s4);
        s.save(s5);
        s.save(s6);
        s.save(s7);
        s.save(s8);


//        return s;
    }

}