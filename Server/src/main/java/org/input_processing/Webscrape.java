package org.input_processing;

import org.json.JSONObject;
import org.codehaus.jettison.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Iterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Webscrape {
    String url;
    // Previously used URLS
    // Lemonade: https://www.simplyrecipes.com/recipes/perfect_lemonade/
    // Turkey: https://www.allrecipes.com/recipe/166160/juicy-thanksgiving-turkey/
    // Chili: https://www.thewholesomedish.com/the-best-classic-chili/
    // Baked Cake Donuts: https://amandascookin.com/baked-cake-donuts/
    // Chicken Alfredo Penne: https://tasty.co/recipe/easy-chicken-alfredo-penne
    public Webscrape(String url) {
        this.url = url;
    }

    public InputRecipe extractRecipe() {
        System.out.println("We're back!!");
        String jsonData = "";
        // Object recipeJson = new Object();
        JSONObject recipeJson = new JSONObject();

        try {
            // TODO: Add error checking
//            Document doc = Jsoup.connect("").get();
            Document doc = Jsoup.connect(url).get();
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
            String recipeString = parser.parse(jsonData).toString();
            recipeJson = new JSONObject(recipeString);
            System.out.println(recipeJson);
        } catch (ParseException e) {
            System.out.println(e);
        }

        InputRecipe inRecipe = new InputRecipe();
        List<String> instructions = new ArrayList<String>();
        HashMap<String, Object> microData = new HashMap<String, Object>();

        if(recipeJson != JSONObject.NULL) {
            Map<String, Object> map = recipeJson.toMap();
            microData.putAll(map);
        } else {
            System.out.println("Probably throw an error");
        }

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
        return inRecipe;
    }

}

