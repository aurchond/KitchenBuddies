package org.input_processing;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
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
    // Turkey: 
    // Chili: https://www.thewholesomedish.com/the-best-classic-chili/
    // Baked Cake Donuts: https://amandascookin.com/baked-cake-donuts/
    // Chicken Alfredo Penne: https://tasty.co/recipe/easy-chicken-alfredo-penne
    // Oven Baked Chicken and Rice: https://www.recipetineats.com/oven-baked-chicken-and-rice/
    // Corn Chowder: https://www.foodandwine.com/recipes/corn-chowder
    // Summer Corn Chowder: https://www.cookingclassy.com/summer-corn-chowder/

    /* URLs Throwing Errors
     * https://www.allrecipes.com/recipe/12682/apple-pie-by-grandma-ople/
     * https://www.allrecipes.com/recipe/166160/juicy-thanksgiving-turkey/
     * 
    */
    public Webscrape(String url) {
        this.url = url;
    }

    public InputRecipe extractRecipe() {
        JSONObject recipeJson = getRecipeObject(this.url);

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
        inRecipe.setRecipeTitle(microData.get("name").toString());
        Object name = microData.get("name");
        if (name != null) {
            inRecipe.setRecipeTitle(name.toString());
        } else {
            // TODO: NEED AN ERROR
        }

        // Total Time - totalTime
        Object totalTime = microData.get("totalTime");
        if (totalTime != null) {inRecipe.setTotalTime(totalTime.toString());}
        
        // Cook Time - cookTime
        Object cookTime = microData.get("cookTime");
        if (cookTime != null) {inRecipe.setCookTime(cookTime.toString());}

        // Prep Time - prepTime
        Object prepTime = microData.get("prepTime");
        if (prepTime != null) {inRecipe.setPrepTime(prepTime.toString());}

        // Ingredients
        Object rIngredients = microData.get("recipeIngredient");
        if (rIngredients != null) {
            List<String> ingredients = new ArrayList<String>();
            for (String ingr: (ArrayList<String>)rIngredients) {
                ingredients.add(ingr);
            }
            inRecipe.setIngredients(ingredients);
            // System.out.println(inRecipe.ingredients);
        }
        
        // Instructions - recipeInstructions
        inRecipe.findInstructions(microData);
        // System.out.println(inRecipe.instructions.toString());

        inRecipe.writeTextFile();
        return inRecipe;
    }

    private JSONObject getRecipeObject(String url) {
        String jsonData = "";
        JSONObject recipeJson = new JSONObject();

        // Get Json data as String from URL
        try {
            // TODO: Add error checking
            Document doc = Jsoup.connect(url).get();
            System.out.println(doc.title());
            Element newsHeadline = doc.select("script[type=application/ld+json]").first();

            if (newsHeadline.data() != null) {
                jsonData = newsHeadline.data();
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        // Parse json data string
        String recipeString = "";
        try {
            JSONParser parser = new JSONParser();
            recipeString = parser.parse(jsonData).toString();

        } catch (ParseException e) {
            System.out.println(e);
        }

        // Convert the json string into a JSONObject
        Object json = null;
        try {
            json = new JSONObject(recipeString);
        } catch (JSONException e) {
            // If it's not a JSON object, try parsing as a JSON array
            json = new JSONArray(recipeString);
        }

        if (json instanceof JSONObject) {
            recipeJson = (JSONObject)json;

        } else if (json instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) json;
            recipeJson = jsonArray.getJSONObject(0);
        }

        Boolean res = foundRecipeType(recipeJson);
        if (res) {
            // System.out.println(recipeJson);
            return recipeJson;
        }

        // More parsing required to find recipe
        try {
            JSONArray graphObj = recipeJson.getJSONArray("@graph");
            for (int i = 0; i < graphObj.length(); i++) {
                JSONObject obj = graphObj.getJSONObject(i);
                // System.out.println(obj);
                if (foundRecipeType(obj)) {
                    // System.out.println(obj);
                    return obj;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("ANOTHER JSON EDGE CASE DETECTED");
        
        return recipeJson;
    }

    private Boolean foundRecipeType(JSONObject obj) {
        try {
            String type = obj.getString("@type");
            // System.out.println(type);
            if (type.equals("Recipe")) {
                return true;
            }
        } catch (JSONException e) {}

        // confirm object is recipe type
        Object types;
        try {
            types = obj.getJSONArray("@type");
        } catch (JSONException e) {
            // Type not found at all
            return false;
        }

        // System.out.println(obj);
        if (types instanceof String && types.equals("Recipe")) {
            return true;
        } else if (types instanceof JSONArray) {
            JSONArray objectTypes = (JSONArray)types;
            for (int i = 0; i < objectTypes.length(); i++) {
                String type = (String)objectTypes.get(i);
                if (type.equals("Recipe")) {
                    return true;
                }
            }
        }
        return false;
    }
}