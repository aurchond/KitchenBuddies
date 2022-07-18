package org.input_processing;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.utilities.database.graph.Step;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map.*;
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
    public static parseJson(String filePath){
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(filePath))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray stepList = (JSONArray) obj;
            System.out.println(stepList);

            //Iterate over employee array
            stepList.forEach( s -> parseStepObject( (JSONObject) s ) );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Step parseStepObject(JSONObject step) throws Exception {
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
        s.setHoldingID(holdingResource);

        Integer holdingID = (Integer) stepObject.get("holdingID");
        System.out.println(holdingID);
        s.setHoldingID(holdingID);

        Integer stepTime = (Integer) stepObject.get("stepTime");
        System.out.println(stepTime);
        s.setStepTime(stepTime);

        Integer userTime = (Integer) stepObject.get("userTime");
        System.out.println(userTime);
        s.setUserTime(userTime);

        List<String> ingredientList = (List<String>) stepObject.get("ingredientList");
        System.out.println(ingredientList);
        s.setIngredientList(ingredientList);

        List<Entry<Integer, String>> ingredientQuantity = (List<Entry<Integer, String>>) stepObject.get("ingredientQuantity");
        System.out.println(ingredientQuantity);
        s.setIngredientQuantity(ingredientQuantity);

        List<String> resourcesRequired = (List<String>) stepObject.get("resourcesRequired");
        System.out.println(resourcesRequired);
        s.setResourcesRequired(resourcesRequired);

        String instructions = (String) stepObject.get("instructions");
        System.out.println(instructions);
        s.setInstructions(instructions);

        return s;
    }
}