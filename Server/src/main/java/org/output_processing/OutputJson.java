package org.output_processing;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.optimization.User;
import org.optimization.UserTask;
import org.server.MealSessionUsersSteps;
import org.server.RecipeStep;
import org.utilities.database.graph.Step;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputJson {

    public static List<MealSessionUsersSteps> userStepsToJson(List<User> users) {
        List<MealSessionUsersSteps> usersStepsList = new ArrayList<>();
        //JSONArray recipeDetails = new JSONArray();

        for (User u : users) {
            List<RecipeStep> recipeSteps = new ArrayList<>();
            //JSONObject userJSON = new JSONObject();
            UserTask task = u.getTail();
            //JSONArray userDetails = new JSONArray();
            while (task != null) {
                Step s = task.getStep();
                RecipeStep recipeStep = new RecipeStep();
                recipeStep.setNumber(s.getNodeID());
                recipeStep.setInstructions(s.getInstructions());
                recipeStep.setIngredientList(s.getIngredientList());
                recipeStep.setIngredientQuantity(s.getIngredientQuantity());
                //TODO: SET dependencyItem and nextUserEmail


                recipeSteps.add(recipeStep);
                task = task.getPrev();
            }
            //TODO: FILL IN notes LATER
            String notes = "";
            MealSessionUsersSteps user = new MealSessionUsersSteps(u.getEmail(), notes, recipeSteps);
            usersStepsList.add(user);
        }

        return usersStepsList;
    }


    public static void userStepsToJsonOld(List<User> users) {
        JSONArray recipeDetails = new JSONArray();

        for (User u : users) {
            JSONObject userJSON = new JSONObject();
            UserTask task = u.getTail();
            JSONArray userDetails = new JSONArray();
            while (task != null) {
                JSONObject taskJSON = new JSONObject();

                JSONObject taskDetails = new JSONObject();
                taskDetails.put("ingredientList", task.getStep().getIngredientList());
                taskDetails.put("ingredientQuantity", task.getStep().getIngredientQuantity());
                taskDetails.put("instructions", task.getStep().getInstructions());

                taskJSON.put(task.getStep().getNodeID(), taskDetails);
                userDetails.add(taskJSON);
                task = task.getPrev();
            }
            userJSON.put(u.getEmail(), userDetails);
            recipeDetails.add(userJSON);
        }

        try (FileWriter file = new FileWriter("res/output/userTaskTest.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(recipeDetails.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
