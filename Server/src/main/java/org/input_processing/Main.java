package org.input_processing;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.utilities.database.graph.Step;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Main {
    // Will handle parsing steps apart before the recipe Creator gets called
    /**
     * 1. Step Creation - This should be done in Input Processing since the NLP should put it in the Data Structure
         *                      we have made for a Step
         * 2. Look for all the dependencies in the steps - This can be done efficiently if we do it alongside step creation
     */
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
    public static void parseJson(
            String filePath,
            List<Step> steps,
            HashMap<String, List<Integer>> ingredients,
            HashMap<String, List<Integer>> resourcesRequired,
            HashMap<String, List<Integer>> holdingResource_Id
    ){
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(filePath))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

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
        Set<Integer> stepNumber = step.keySet();
        if(stepNumber.size() != 1){
            throw new Exception("there are more than one step to process");
        }
        Step s = new Step();
        //Get employee object within list
        Integer stepId = stepNumber.iterator().next();
        System.out.println(stepId);
        s.setStepID(stepId);
        JSONObject stepObject = (JSONObject) step.get(stepId);

        //Get employee first name
        Boolean prepStep = (Boolean) stepObject.get("prepStep");
        System.out.println(prepStep);
        s.setPrepStep(prepStep);

        String holdingResource = (String) stepObject.get("holdingResource");
        System.out.println(holdingResource);
        s.setHoldingResource(holdingResource);

        Integer holdingID = (Integer) stepObject.get("holdingID");
        System.out.println(holdingID);
        s.setHoldingID(holdingID);

        if(holdingResource_Id.containsKey(holdingResource+"_"+holdingID)){
            holdingResource_Id.get(holdingResource+"_"+holdingID).add(stepId);
        }else {
            holdingResource_Id.put(holdingResource+"_"+holdingID,List.of(stepId));
        }

        Integer stepTime = (Integer) stepObject.get("stepTime");
        System.out.println(stepTime);
        s.setStepTime(stepTime);

        Integer userTime = (Integer) stepObject.get("userTime");
        System.out.println(userTime);
        s.setUserTime(userTime);

        List<String> ingredientList = (List<String>) stepObject.get("ingredientList");
        System.out.println(ingredientList);
        s.setIngredientList(ingredientList);
        for (String ingredient: ingredientList) {
            if(ingredients.containsKey(ingredient)){
                ingredients.get(ingredient).add(stepId);
            }else {
                ingredients.put(ingredient,List.of(stepId));
            }
        }

        List<Float> ingredientQuantity = (List<Float>) stepObject.get("ingredientQuantity");
        System.out.println(ingredientQuantity);
        s.setIngredientQuantity(ingredientQuantity);

        List<String> rRequired = (List<String>) stepObject.get("resourcesRequired");
        System.out.println(rRequired);
        s.setResourcesRequired(rRequired);

        for (String resource: rRequired) {
            if(resourcesRequired.containsKey(resource)){
                resourcesRequired.get(resource).add(stepId);
            }else {
                resourcesRequired.put(resource,List.of(stepId));
            }
        }

        String instructions = (String) stepObject.get("instructions");
        System.out.println(instructions);
        s.setInstructions(instructions);

        steps.add(s);
    }
}