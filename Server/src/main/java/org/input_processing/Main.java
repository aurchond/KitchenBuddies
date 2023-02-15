package org.input_processing;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.utilities.database.graph.Step;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    // Will handle parsing steps apart before the recipe Creator gets called
    /**
     * 1. Step Creation - This should be done in Input Processing since the NLP should put it in the Data Structure
         *                      we have made for a Step
         * 2. Look for all the dependencies in the steps - This can be done efficiently if we do it alongside step creation
     */
    public static void main(String[] args) throws IOException {
        //

//        Webscrape test_w = new Webscrape();

        parseInstructionsPython();


//        OCR test = new OCR();
//        test.TestOCR();
//        List<Step> steps = new ArrayList<Step>();
//        HashMap<String, List<Integer>> ingredients = new HashMap<String, List<Integer>>();//<ingredient, List<StepId>>
//        HashMap<String, List<Integer>> resourcesRequired = new HashMap<String, List<Integer>>();//<tool, List<StepId>>
//        HashMap<String, List<Integer>> holdingResource_Id = new HashMap<String, List<Integer>>();//<holdingResource, List<StepId>>
//        String name = new File(".").getCanonicalPath();
//        System.out.println("Test "+ name);
//        parseJson(
//                "res/input/fried_rice.json",
//                steps,
//                ingredients,
//                resourcesRequired,
//                holdingResource_Id
//        );
//        System.out.println(Arrays.asList(ingredients));
//        System.out.println(Arrays.asList(resourcesRequired));
//        System.out.println(Arrays.asList(holdingResource_Id));
    }

    public static void parseInstructionsPython(){
        try {
            long startTime = System.nanoTime();
            String currentDirectory = System.getProperty("user.dir");
            System.out.println("Current directory: " + currentDirectory);
            String os = System.getProperty("os.name");
            String activateEnv = "";
            String command = "";
            String parameter = "";
            String script = "";
            if (os.toLowerCase().startsWith("windows")) {
                // for PC users (local development)
                script = ".\\Py_Text_Processing\\main.py";
                activateEnv = ".\\Py_Text_Processing\\kb_text\\Scripts\\activate.bat";
                command = "cmd /c \"" + activateEnv + " & python " + script + "\"";
            } else {
                // for Server
                script = "./Py_Text_Processing/main.py";
                command = "python3 " + script + " " + parameter;
            }

            System.out.println("Running python process");
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
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
            HashMap<String, List<Integer>> ingredients,
            HashMap<String, List<Integer>> resourcesRequired,
            HashMap<String, List<Integer>> holdingResource_Id
    ){
        JSONParser jsonParser = new JSONParser();
        Path path = Path.of(filePath);


        try
        {
            String json = Files.readString(path);
            //Read JSON file
            Object obj = jsonParser.parse(json);

            JSONArray stepList = (JSONArray) obj;
            System.out.println(stepList);

            stepList.forEach( s -> {
                try {
                    parseStepObject( (JSONObject) s, steps, ingredients, resourcesRequired,holdingResource_Id );
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
            HashMap<String, List<Integer>> ingredients,
            HashMap<String, List<Integer>> resourcesRequired,
            HashMap<String, List<Integer>> holdingResource_Id
    ) throws Exception {
        Set<String> stepNumber = step.keySet();
        if(stepNumber.size() != 1){
            throw new Exception("there are more than one step to process");
        }
        Step s = new Step();
        //Get employee object within list
        String key = stepNumber.iterator().next();
        Integer stepId = Integer.parseInt(key);
        System.out.println(stepId);
        s.setStepID(stepId);
        JSONObject stepObject = (JSONObject) step.get(key);

        //Get employee first name
        Boolean prepStep = (Boolean) stepObject.get("prepStep");
        System.out.println(prepStep);
        s.setPrepStep(prepStep);

        String holdingResource = (String) stepObject.get("holdingResource");
        System.out.println(holdingResource);
        s.setHoldingResource(holdingResource);

        Integer holdingID = ((Long)stepObject.get("holdingID")).intValue();
        System.out.println(holdingID);
        s.setHoldingID(holdingID);

        holdingResource_Id.computeIfAbsent(holdingResource+"_"+holdingID, k -> new ArrayList<>()).add(stepId);
        resourcesRequired.computeIfAbsent(holdingResource, k -> new ArrayList<>()).add(stepId);

        Integer stepTime = ((Long)stepObject.get("stepTime")).intValue();
        System.out.println(stepTime);
        s.setStepTime(stepTime);

        Integer userTime = ((Long)stepObject.get("userTime")).intValue();
        System.out.println(userTime);
        s.setUserTime(userTime);

        List<String> ingredientList = (List<String>) stepObject.get("ingredientList");
        System.out.println(ingredientList);
        s.setIngredientList(ingredientList);
        for (String ingredient: ingredientList) {
            ingredients.computeIfAbsent(ingredient, k -> new ArrayList<>()).add(stepId);
        }
        System.out.println(Arrays.asList(ingredients));

        List<Float> ingredientQuantity = (List<Float>) stepObject.get("ingredientQuantity");
        System.out.println(ingredientQuantity);
        s.setIngredientQuantity(ingredientQuantity);

        List<String> rRequired = (List<String>) stepObject.get("resourcesRequired");
        System.out.println(rRequired);
        s.setResourcesRequired(rRequired);

        for (String resource: rRequired) {
            resourcesRequired.computeIfAbsent(resource, k -> new ArrayList<>()).add(stepId);
        }

        String instructions = (String) stepObject.get("instructions");
        System.out.println(instructions);
        s.setInstructions(instructions);

        steps.add(s);
    }
}