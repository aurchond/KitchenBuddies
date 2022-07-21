package org.output_processing;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.optimization.User;
import org.optimization.UserTask;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void userStepsToJson(List<User> users) {
        JSONArray recipeDetails = new JSONArray();

        for (User u: users) {
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
            userJSON.put(u.getName(), userDetails);
            recipeDetails.add(userJSON);
        }

        try (FileWriter file = new FileWriter("userTaskTest.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(recipeDetails.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
