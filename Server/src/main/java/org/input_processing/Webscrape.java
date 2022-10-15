package org.input_processing;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Webscrape {

    public Webscrape() {
        System.out.println("We're back!!");
        String jsonData = "";
        Object recipeJson = new Object();

        try {
            // TODO: Add error checking
//            Document doc = Jsoup.connect("https://www.simplyrecipes.com/recipes/perfect_lemonade/").get();
            Document doc = Jsoup.connect("https://www.allrecipes.com/recipe/166160/juicy-thanksgiving-turkey/").get();
            System.out.println(doc.title());
            Element newsHeadline = doc.select("script[type=application/ld+json]").first();

            if (newsHeadline.data() != null) {
                jsonData = newsHeadline.data();
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        try {
            JSONParser parser = new JSONParser();
            recipeJson = parser.parse(jsonData);
            System.out.println(recipeJson);
        } catch (ParseException e) {
            System.out.println(e);
        }

        InputRecipe inRecipe = new InputRecipe();
//        HashMap<String, String> map = (HashMap<String, String>)recipeJson[0];
        JSONArray test = (JSONArray)recipeJson;
        HashMap<String, Object> microData = (HashMap<String, Object>)test.get(0);

        // Title - name
        System.out.println(microData.get("name"));
        inRecipe.setRecipeTitle(microData.get("name").toString());

        // Total Time - totalTime
        System.out.println(microData.get("totalTime"));
        inRecipe.setTotalTime(microData.get("totalTime").toString());

        // Cook Time - cookTime
        System.out.println(microData.get("cookTime"));
        inRecipe.setCookTime(microData.get("cookTime").toString());

        // Prep Time - prepTime
        System.out.println(microData.get("prepTime"));
        inRecipe.setPrepTime(microData.get("prepTime").toString());

        // Ingredients
        System.out.println(microData.get("recipeIngredient"));
        List<String> ingredients = new ArrayList<String>();

        for (String ingr: (ArrayList<String>)microData.get("recipeIngredient")) {
            ingredients.add(ingr);
        }
        inRecipe.setIngredients(ingredients);
        System.out.println(inRecipe.ingredients);

        // Instructions - recipeInstructions
        List<String> instructions = new ArrayList<String>();
        for (HashMap<String, String> step:
                (ArrayList<HashMap<String, String>>) microData.get("recipeInstructions")) {
            if (step.get("@type").equals("HowToStep")) {
                instructions.add(step.get("text"));
            }
            // Figure out how to parse HowToSection
        }
        inRecipe.setInstructions(instructions);
        System.out.println(inRecipe.instructions.toString());

        inRecipe.writeTextFile();
    }
}
