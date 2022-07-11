package org.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.neo4j.graphdb.Path;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.transaction.Transaction;
import org.neo4j.ogm.config.Configuration;

public class Main {
    public static void main(String[] args) {

        buildSampleRecipe();

//        System.out.println(session.countEntitiesOfType(Step.class) + " stations saved");
//        getRoute("Step 1", "Step 3", session);
//        System.out.println(session.load(Step.class, s3.getRecipeID()).getStepID());

    }

    private static void buildSampleRecipe() {
        System.out.println("Hello world!");
        String uri = "neo4j+s://db42e3f1.databases.neo4j.io";
        Configuration configuration = new Configuration.Builder()
                .uri(uri)
                .credentials("neo4j", "9hCaQ7nmAyf5AAkUwjrk5lY8ejC61PYa2-4-zLBc6hg")
                .build();
        SessionFactory sessionFactory = new SessionFactory(configuration, "org.test");
        final Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        tx = createAsparagus(tx, session);
        tx.close();
    }

    //https://www.simplyrecipes.com/recipes/asparagus/
    private static Transaction createAsparagus(Transaction tx, Session s) {
        // Step 1
        // Boil water in saucepan
        List<String> r1_ingredients = new ArrayList<String>();
        r1_ingredients.add("salted water");

        List<Number> r1_quantity = new ArrayList<Number>();
        r1_quantity.add(0.5);

        List<String> r1_tools = new ArrayList<String>();
        Step s1 = new Step(25L, 1, true, "saucepan",
                0, 1, 10, r1_ingredients, r1_quantity, r1_tools);

        // Step 2
        // Prepare asparagus
        // Prepare the asparagus while the water is heating. Rinse them thoroughly. Break off any tough, white bottoms and discard.
        //
        //Cut the spears into 1- to 2-inch sections, slicing the asparagus at a slight diagonal.
        List<String> r2_ingredients = new ArrayList<String>();
        r2_ingredients.add("asparagus");
        List<Number> r2_quantity = new ArrayList<Number>();
        r2_quantity.add(1);

        List<String> r2_tools = new ArrayList<String>();
        r2_tools.add("knife");

        Step s2 = new Step(25L, 2, true, "cutting board",
                0, 5, 5, r2_ingredients, r2_quantity, r2_tools);

        // Step 3
        // Boil asparagus and lower heat
        List<String> r3_ingredients = new ArrayList<String>();
        r2_ingredients.add("asparagus");
        List<Number> r3_quantity = new ArrayList<Number>();
        r2_quantity.add(1);

        Step s3 = new Step(25L, 3, false, "saucepan",
                0, 10, 20, r2_ingredients, r2_quantity, r2_tools);

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
                0, 5, 25, r4_ingredients, r4_quantity, r4_tools);

        // Step 1 -> Step 3 with 10 timeleft
        s1.addConnection(s3, 10);
        // Step 2 -> Step 3 with 5 timeleft
        s2.addConnection(s3, 5);
        // Step 3 -> Step 4 with 20 timeleft
        s3.addConnection(s4, 20);

        s.save(s1);
        s.save(s2);
        s.save(s3);
        tx.commit();

        return tx;
    }
}