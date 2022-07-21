package org.utilities.database.graph;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        buildSampleRecipe();

//        System.out.println(session.countEntitiesOfType(Step.class) + " stations saved");
//        getRoute("Step 1", "Step 3", session);
//        System.out.println(session.load(Step.class, s3.getRecipeID()).getStepID());

    }

    private static void buildSampleRecipe() {
        System.out.println("Hello world!");
        final Session session = RecipeHelper.createSession();
        Transaction tx = session.beginTransaction();

        // These recipes are hard coded for testing purposes
//        createAsparagus(session);
//        createChicken(session);
        createBakedRotini(session);
        createFriedRice(session);
        createSalmon(session);

        tx.commit();
        tx.close();
        //Do you have to close the session?
    }

    /*
    //https://www.simplyrecipes.com/recipes/asparagus/
    public static void createAsparagus(Session s) {
        // Step 1
        // Boil water in saucepan
        List<String> r1_ingredients = new ArrayList<String>();
        r1_ingredients.add("salted water");

        List<Map.Entry<Integer, String>> r1_quantity = new ArrayList<Map.Entry<Integer, String>>();
        r1_quantity.add(new AbstractMap.SimpleEntry<>(0.5, "cups"));

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
        List<Map.Entry<Integer, String>> r2_quantity = new ArrayList<Map.Entry<Integer, String>>();
        r2_quantity.add(1);

        List<String> r2_tools = new ArrayList<String>();
        r2_tools.add("knife");

        Step s2 = new Step(25L, 2, true, "cutting board",
                0, 5, 5, r2_ingredients, r2_quantity, r2_tools);

        // Step 3
        // Boil asparagus and lower heat
        List<String> r3_ingredients = new ArrayList<String>();
        r2_ingredients.add("asparagus");
        List<Map.Entry<Integer, String>> r3_quantity = new ArrayList<Map.Entry<Integer, String>>();
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
        List<Map.Entry<Integer, String>> r4_quantity = new ArrayList<Map.Entry<Integer, String>>();
        r2_quantity.add(new AbstractMap.SimpleEntry<>(1, "stalk"));
        r2_quantity.add(new AbstractMap.SimpleEntry<>(2, "tbs"));
        r2_quantity.add(new AbstractMap.SimpleEntry<>(2, "tbs"));
        r2_quantity.add(new AbstractMap.SimpleEntry<>(1, "tbs"));

        List<String> r4_tools = new ArrayList<String>();
        r2_tools.add("spoon");

        Step s4 = new Step((Long) 25L, (Integer) 4, (Boolean) false, "bowl",
                (Integer) 0, (Integer) 5, (Integer) 25, r4_ingredients, r4_quantity, r4_tools);

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
                0, 5, 5, ingredients, quantity, tools);

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 2
        // Beat eggs/milk\
        ingredients.add("Eggs");
        ingredients.add("Milk");

        quantity.add(1);
        quantity.add(0.25);

        tools.add("Spoon");
        Step s2 = new Step(26L, 2, true, "bowl",
                0, 5, 5, ingredients, quantity, tools);

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
                0, 10, 15, ingredients, quantity, tools);

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 4
        // Heat oven
        Step s4 = new Step(26L, 4, false, "none",
                0, 5, 1);

        // Step 5
        // Heat pan
        Step s5 = new Step(26L, 5, false, "pan",
                0, 1, 5);

        // Step 6
        // Pan fry chicken
        ingredients.add("Chicken");
        quantity.add(8);
        tools.add("Spatula");
        Step s6 = new Step(26L, 6, true, "none",
                0, 5, 20, ingredients, quantity, tools);

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 7
        // Bake chicken
        ingredients.add("Chicken");
        quantity.add(8);
        tools.add("Spatula");
        Step s7 = new Step(26L, 7, false, "baking sheet",
                0, 2, 22, ingredients, quantity, tools);

        // Step 8
        // Take out food
        Step s8 = new Step(26L, 8, false, "baking sheet",
                0, 2, 50);


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
    } */

    //https://amindfullmom.com/5-ingredient-baked-rotini/
    public static void createBakedRotini(Session s) {
        // Step 1
        // Boil water
        List<String> ingredients = new ArrayList<String>();
        List<Float> quantity = new ArrayList<Float>();
        List<String> tools = new ArrayList<String>();

        ingredients.add("cups of water");
        ingredients.add("teaspoon of salt");
        quantity.add(5.0f);
        quantity.add(0.5f);

        Step s1 = new Step(122L, 1, true, "pot",
                0, 10, 0, ingredients, quantity, tools, 1, "boil");

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 2
        // Cook pasta
        ingredients.add("ounces of rotini");
        quantity.add(16f);
        tools.add("spoon");

        Step s2 = new Step(122L, 2, false, "pot",
                0, 5, 10, ingredients, quantity, tools, 5, "cook");

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 3
        // Drain pasta
        ingredients.add("ounces of rotini");
        ingredients.add("cups of pasta liquid");
        quantity.add(16f);
        quantity.add(0.5f);
        tools.add("drainer");

        Step s3 = new Step(122L, 3, false, "pot",
                0,  1, 15, ingredients, quantity, tools, 1, "drain");

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 4
        // Mix in cheese and other ingredients
        ingredients.add("ounces of rotini");
        ingredients.add("cups of pasta liquid");
        ingredients.add("cups of cottage cheese");
        ingredients.add("cups of spaghetti sauce");
        ingredients.add("whisked egg");
        ingredients.add("cups of shredded cheese");
        quantity.add(16f);
        quantity.add(0.5f);
        quantity.add(2f);
        quantity.add(4f);
        quantity.add(1f);
        quantity.add(1f);
        tools.add("spoon");

        Step s4 = new Step(122L, 4, false, "pot",
                0, 4, 16, ingredients, quantity, tools, 4, "mix");

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 5
        // Preheat oven
        Step s5 = new Step(122L, 5, false, "oven",
                2, 5, 0, 1);

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 6
        // Put pasta in pan
        ingredients.add("casserole"); //NOTE: changed this to represent combined previous steps
        ingredients.add("cups of shredded cheese");
        quantity.add(1f);
        quantity.add(1f);
        tools.add("oven");

        Step s6 = new Step(122L, 6, false, "9x13 pan",
                2, 30, 20, ingredients, quantity, tools, 1, "transfer");

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 7
        // Bake pasta in oven
        ingredients.add("casserole");
        quantity.add(1f);
        tools.add("oven gloves");

        Step s7 = new Step(122L, 7, false, "9x13 pan",
                2, 1, 50, ingredients, quantity, tools, 1, "bake");

//        s1.addConnection(s2,10);
//        s2.addConnection(s3, 5);
//        s3.addConnection(s4, 1);
//        s4.addConnection(s6, 4);
//        s5.addConnection(s6, 5);
//        s6.addConnection(s7, 30);

        s2.addConnection(s1,-1);
        s3.addConnection(s2, -1);
        s4.addConnection(s3, -1);
        s6.addConnection(s4, -1);
        s6.addConnection(s5, -1);
        s7.addConnection(s6, -1);

        s.save(s1);
        s.save(s2);
        s.save(s3);
        s.save(s4);
        s.save(s5);
        s.save(s6);
        s.save(s7);

    }

    // https://aggieskitchen.com/5-ingredient-vegetable-fried-brown-rice/
    public static void createFriedRice(Session s) {
        // Step 1
        // Heat oil in pan
        List<String> ingredients = new ArrayList<String>();
        ingredients.add("teaspoon of coconut oil");

        List<Float> quantity = new ArrayList<Float>();
        quantity.add(1f);

        List<String> tools = new ArrayList<String>();
        Step s1 = new Step(120L, 1, true, "wok",
                0, 2, 0, ingredients, quantity, tools, 2, "heat");

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 2
        // Cook vegetables
        ingredients.add("cup of frozen mixed vegetables");
        quantity.add(1f);
        tools.add("spatula");

        // timeleft = prevstepTime + prevTimeLeft

        Step s2 = new Step(120L, 2, false, "wok",
                0, 2, 2, ingredients, quantity, tools, 2, "cook");

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 3
        // Cook brown rice
        ingredients.add("cups of brown rice");
        ingredients.add("cups of water");
        quantity.add(2f);
        quantity.add(4f);
        tools.add("strainer");

        Step s3 = new Step(120L, 3, true, "pot",
                0, 20, 0, ingredients, quantity, tools, 2, "cook");

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 4
        // Add soy sauce and brown rice, cook veggies
        ingredients.add("cups of soy sauce");
        ingredients.add("cups of brown rice");
        quantity.add(0.33f);
        quantity.add(2f);

        Step s4 = new Step(120L, 4, false, "wok",
                0, 5, 20, ingredients, quantity, tools, 5, "cook");

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 5
        // Cook eggs
        ingredients.add("whisked eggs");
        quantity.add(2f);
        tools.add("spatula");

        Step s5 = new Step(120L, 5, false, "wok",
                0, 1, 25, ingredients, quantity, tools, 1, "cook");

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 6
        // Season and serve
        ingredients.add("teaspoon of salt");
        ingredients.add("teaspoon of pepper");
        ingredients.add("tablespoon of sriracha");
        quantity.add(0.5f);
        quantity.add(0.5f);
        quantity.add(1f);

        Step s6 = new Step(120L, 6, false, "wok",
                0, 1, 26, ingredients, quantity, tools, 1, "season");

        s2.addConnection(s1, -1);
        s4.addConnection(s2, -1);
        s4.addConnection(s3, -1);
        s5.addConnection(s4, -1);
        s6.addConnection(s5, -1);

//        s1.addConnection(s2, 2);
//        s2.addConnection(s4, 2);
//        s3.addConnection(s4, 20);
//        s4.addConnection(s5, 5);
//        s5.addConnection(s6, 1);

        s.save(s1);
        s.save(s2);
        s.save(s3);
        s.save(s4);
        s.save(s5);
        s.save(s6);
    }

    // https://thestayathomechef.com/easy-5-ingredient-baked-salmon/
    public static void createSalmon(Session s) {
        // Step 1
        // Prepare baking sheet
        List<String> ingredients = new ArrayList<String>();
        ingredients.add("aluminum foil");

        List<Float> quantity = new ArrayList<Float>();
        quantity.add(1f);

        List<String> tools = new ArrayList<String>();
        Step s1 = new Step(124L, 1, true, "baking sheet",
                0, 2, 0, ingredients, quantity, tools, 2, "prepare");

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 2
        // Combine Ingredients
        ingredients.add("cup of melted salted butter");
        ingredients.add("crushed garlic cloves");
        ingredients.add("tablespoons finely chopped dill");
        ingredients.add("tablespoons lemon juice");
        quantity.add(0.5f);
        quantity.add(8f);
        quantity.add(2f);
        quantity.add(4f);
        tools.add("whisk");

        Step s2 = new Step(124L, 2, false, "glass measuring cup",
                0, 5, 0, ingredients, quantity, tools, 5, "mix");

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 3
        // Combine butter mix and salmon
        // TODO: How do we refer to something that is mixed?
        ingredients.add("butter mixture");
        ingredients.add("salmon fillet");
        quantity.add(1f);
        quantity.add(1f);

        Step s3 = new Step(124L, 3, false, "baking sheet",
                0, 3, 5, ingredients, quantity, tools, 3, "combine");

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 4
        // Preheat oven //TODO: what is our definition of prep step? cutting + preheating
        Step s4 = new Step(124L, 4, false, "oven",
                2, 5, 0, 1);

        ingredients.clear();
        quantity.clear();
        tools.clear();

        // Step 5 + 6
        // Bake fillet in oven TODO: When we have our food in a container which goes into another resource, how do we define this?
        // (i.e. salmon on baking sheet in oven)
        ingredients.add("salmon fillet");
        quantity.add(1f);

        Step s5 = new Step(124L, 5, true, "sheet",
                2, 15, 8, ingredients, quantity, tools, 1, "bake");

        Step s6 = new Step(124L, 6, false, "oven",
                2, 1, 23, 1);

        s3.addConnection(s1, -1);
        s3.addConnection(s2, -1);
        s5.addConnection(s3, -1);
        s5.addConnection(s4, -1);
        s6.addConnection(s5,  -1);
//        s1.addConnection(s3, 1);
//        s2.addConnection(s3, 5);
//        s3.addConnection(s5, 3);
//        s4.addConnection(s5, 5);
//        s5.addConnection(s6,  15);

        s.save(s1);
        s.save(s2);
        s.save(s3);
        s.save(s4);
        s.save(s5);
        s.save(s6);
    }

}
