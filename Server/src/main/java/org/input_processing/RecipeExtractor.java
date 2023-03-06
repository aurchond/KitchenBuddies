package org.input_processing;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.recipe_processing.Recipe;
import org.server.RecipeInfo;
import org.server.RecipeInput;
import org.utilities.database.graph.Step;
import static org.utilities.database.graph.RecipeHelper.*;
import static org.recipe_processing.RecipeCreator.createRecipe;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.utilities.database.relational.MySqlConnection.addToAllRecipes;
import static org.utilities.database.relational.MySqlConnection.addToAllRecipesFromText;
import static org.utilities.database.relational.MySqlConnection.addUserLinkedRecipe;
import static org.utilities.database.relational.MySqlConnection.recipeInGraphDB;

public class RecipeExtractor {

    public static RecipeInfo parseUserRecipe(RecipeInput recipeInput) throws IOException {

        // Covert data to InputRecipe
        List<String> instructions = new ArrayList<>();
        for (String step : recipeInput.getInstructionList()) {
            if (step.charAt(step.length() - 1) == '.') {
                instructions.add(step);
            } else {
                instructions.add(step + ".");
            }
        }

        //TODO: might need to format the file name to not have spaces or something
        // Input the json from our app with title, totalTime, ingredients, instructions

        InputRecipe inRecipe = new InputRecipe();
        inRecipe.setRecipeTitle(recipeInput.getRecipeName());
        inRecipe.setTotalTime(recipeInput.getTotalTime());
        inRecipe.setIngredients(recipeInput.getIngredientList());
        inRecipe.setInstructions(instructions);
        inRecipe.writeTextFile();

        // Add to relational database
        Long recipeID = addToAllRecipesFromText(inRecipe.getRecipeTitle(), inRecipe.convertIngredientsToString(), inRecipe.getTotalTime());

        // Add recipe to UserLinkedRecipes
        Boolean res = addUserLinkedRecipe(recipeInput.getUserEmail(), recipeID);
        if (!res) {
            // Might enter here if URL already exists, but recipe not in graph db
            // Do nothing
        }

        // Run RecipeExtractor
        res = ExtractRecipe(inRecipe, recipeID);
        // return Success or Failure
        if (res) {
            return new RecipeInfo(recipeID, inRecipe.getRecipeTitle(), inRecipe.ingredients, inRecipe.getTotalTime(), null);
        }
        return null;
    }

    public static RecipeInfo parseRecipeUrl(String email, String url) {
        // String url = "https://amandascookin.com/baked-cake-donuts/";
        // Scrape website and place info in text file within Py_Text_Processing/Input folder
        Webscrape scraper = new Webscrape(url);
        InputRecipe inRecipe = scraper.extractRecipe();

        // Add to relational database
        Long recipeID = addToAllRecipes(inRecipe.getRecipeTitle(), url, inRecipe.convertIngredientsToString(), inRecipe.getTotalTime());

        // Add recipe to UserLinkedRecipes
        Boolean res = addUserLinkedRecipe(email, recipeID);
        if (!res) {
            // Might enter here if URL already exists, but recipe not in graph db
            // Do nothing
        }

        if (inRecipe.recipeFile == null) {
            return null;
        }

        // Process Text and Add to graph database
        res = ExtractRecipe(inRecipe, recipeID);
        if (res) {
            return new RecipeInfo(recipeID, inRecipe.getRecipeTitle(), inRecipe.ingredients, inRecipe.getTotalTime(), null);
        }
        return null;
    }

    private static Boolean ExtractRecipe(InputRecipe inRecipe, long recipeID) {
        // TODO: Place basic multithreading (1 thread for steps, other thread for placing recipe in database)
        // Use Python to process the recipe instructions, step file exported to json file within Py_Text_Processing/Output folder
        parseInstructionsPython(inRecipe.recipeFile + ".txt");

        // Retrieve recipe steps 
        List<Step> steps = new ArrayList<Step>();
        HashMap<String, List<Integer>> baseIngredients = new HashMap<String, List<Integer>>();//<ingredient, List<StepId>>
        HashMap<String, List<Integer>> resourcesRequired = new HashMap<String, List<Integer>>();//<tool, List<StepId>>
        HashMap<String, List<Integer>> holdingResource_Id = new HashMap<String, List<Integer>>();//<holdingResource, List<StepId>>
        HashMap<String, List<Integer>> lineNumbers = new HashMap<String, List<Integer>>();

        parseJson(
                "Py_Text_Processing/output/" + inRecipe.recipeFile + ".json",
                steps,
                baseIngredients,
                resourcesRequired,
                holdingResource_Id,
                lineNumbers
        );

        // Metadata = details about a recipe
        
        Recipe outRecipe = createRecipe(steps, baseIngredients, resourcesRequired, holdingResource_Id, lineNumbers, recipeID);// String will be formatted as "holdingResource_holdingId"
        outRecipe.setRecipeName(inRecipe.recipeTitle);
        saveRecipe(outRecipe, outRecipe.getRecipeName());
        Boolean res = recipeInGraphDB(recipeID);

        System.out.println(Arrays.asList(baseIngredients));
        System.out.println(Arrays.asList(resourcesRequired));
        System.out.println(Arrays.asList(holdingResource_Id));
        if (res) {return true;} else {return false;}
    }

    private static void parseInstructionsPython(String recipeFile) {
        try {
            long startTime = System.nanoTime();
            String currentDirectory = System.getProperty("user.dir");
            // System.out.println("Current directory: " + currentDirectory);
            String os = System.getProperty("os.name");
            String activateEnv = "";
            String command = "";
            String parameter = recipeFile;
            String script = "";
            if (os.toLowerCase().startsWith("windows")) {
                // for PC users (local development)
                script = ".\\Py_Text_Processing\\main.py";
                activateEnv = ".\\Py_Text_Processing\\kb_text\\Scripts\\activate.bat";
                command = "cmd /c \"" + activateEnv + " & python " + script + " " + parameter + "\"";
            } else {
                // for Server
                script = "./Py_Text_Processing/main.py";
                command = "python3 " + script + " " + parameter;
            }

            System.out.println("Running " + command);
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                // System.out.println(line);
            }
            in.close();

            process.waitFor();
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;
            System.out.println("Duration: " + duration + " milliseconds");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void parseJson(
            String filePath,
            List<Step> steps,
            HashMap<String, List<Integer>> baseIngredients,
            HashMap<String, List<Integer>> resourcesRequired,
            HashMap<String, List<Integer>> holdingResource_Id,
            HashMap<String, List<Integer>> lineNumbers
    ){
        JSONParser jsonParser = new JSONParser();
        Path path = Path.of(filePath);


        try {
            String json = Files.readString(path);
            //Read JSON file
            Object obj = jsonParser.parse(json);

            JSONArray stepList = (JSONArray) obj;
            // System.out.println(stepList);

            stepList.forEach(s -> {
                try {
                    parseStepObject( (JSONObject) s, steps, baseIngredients, resourcesRequired,holdingResource_Id, lineNumbers );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void parseStepObject(
            JSONObject step,
            List<Step> steps,
            HashMap<String, List<Integer>> baseIngredients,
            HashMap<String, List<Integer>> resourcesRequired,
            HashMap<String, List<Integer>> holdingResource_Id,
            HashMap<String, List<Integer>> lineNumbers
    ) throws Exception {
        Set<String> stepNumber = step.keySet();
        if (stepNumber.size() != 1) {
            throw new Exception("there are more than one step to process");
        }
        Step s = new Step();
        //Get employee object within list
        String key = stepNumber.iterator().next();
        Integer stepId = Integer.parseInt(key);
        // System.out.println(stepId);
        s.setStepID(stepId);
        JSONObject stepObject = (JSONObject) step.get(key);

        //Get employee first name
        Boolean prepStep = (Boolean) stepObject.get("prepStep");
        // System.out.println(prepStep);
        s.setPrepStep(prepStep);

        String holdingResource = (String) stepObject.get("holdingResource");
        // System.out.println(holdingResource);
        s.setHoldingResource(holdingResource);

        Integer holdingID = ((Long) stepObject.get("holdingID")).intValue();
        // System.out.println(holdingID);
        s.setHoldingID(holdingID);

        holdingResource_Id.computeIfAbsent(holdingResource+"_"+holdingID, k -> new ArrayList<>()).add(stepId);
        // resourcesRequired.computeIfAbsent(holdingResource, k -> new ArrayList<>()).add(stepId);

        Integer lineNumber = ((Long)stepObject.get("lineNumber")).intValue();
        // System.out.println(lineNumber);
        lineNumbers.computeIfAbsent(Integer.toString(lineNumber), k -> new ArrayList<>()).add(stepId);

        Integer stepTime = ((Long) stepObject.get("stepTime")).intValue();
        // System.out.println(stepTime);
        s.setStepTime(stepTime);

        Integer userTime = ((Long) stepObject.get("userTime")).intValue();
        // System.out.println(userTime);
        s.setUserTime(userTime);

        // TODO: Make the ingredientList the nice ingredients, not the base word
        List<String> ingredientList = (List<String>) stepObject.get("baseIngredients");
        if (ingredientList == null) {
            ingredientList = new ArrayList<String>();
        }
        s.setIngredientList(ingredientList);
        // System.out.println(ingredientList);
        for (String ingredient : ingredientList) {
            baseIngredients.computeIfAbsent(ingredient, k -> new ArrayList<>()).add(stepId);
        }
        // System.out.println(Arrays.asList(baseIngredients));

        List<Float> ingredientQuantity = (List<Float>) stepObject.get("ingredientQuantity");
        // System.out.println(ingredientQuantity);
        s.setIngredientQuantity(ingredientQuantity);

        List<String> rRequired = (List<String>) stepObject.get("resourcesRequired");
        // System.out.println(rRequired);
        s.setResourcesRequired(rRequired);

        for (String resource : rRequired) {
            resourcesRequired.computeIfAbsent(resource, k -> new ArrayList<>()).add(stepId);
        }

        String instructions = (String) stepObject.get("instructions");
        // System.out.println(instructions);
        s.setInstructions(instructions);

        steps.add(s);
    }
}