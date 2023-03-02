package org.input_processing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

import org.json.JSONObject;
import org.json.JSONArray;

public class InputRecipe {
    String recipeTitle;
    String recipeFile;
    // Time in minutes
    Integer totalTime;
    Integer cookTime;
    Integer prepTime;
    List<String> ingredients;
    List<String> instructions;

    public void InputRecipe(){
        this.recipeTitle = "";
        this.totalTime = 0;
        this.cookTime = 0;
        this.prepTime = 0;
        this.ingredients = new ArrayList<>();
        this.instructions = new ArrayList<>();
        this.recipeFile = "";
    }

    public String getRecipeTitle() { return this.recipeTitle; }

    public Integer getTotalTime() { return totalTime; }

    public Integer getCookTime() { return cookTime;}

    public Integer getPrepTime() { return prepTime;}

    public List<String> getIngredients() { return ingredients; }

    public List<String> getInstructions() { return instructions; }

    public void setRecipeTitle(String recipeTitle) { this.recipeTitle = recipeTitle; }

    public void setRecipeFile(String recipeFile) { this.recipeFile = recipeFile; }

    public void setTotalTime(String isoTotalTime) {
        Duration duration = Duration.parse(isoTotalTime);
        long cookTime = duration.toMinutes();
        // Integer totalTime = this.convertISOMinute(isoTotalTime);
        this.totalTime = (int)cookTime;
    }

    public void setCookTime(String isoCookTime) {
        Duration duration = Duration.parse(isoCookTime);
        long cookTime = duration.toMinutes();
        // System.out.println(minutes);
        // Integer cookTime = this.convertISOMinute(isoCookTime);
        this.cookTime = (int)cookTime;
    }

    public void setPrepTime(String isoPrepTime) {
        Duration duration = Duration.parse(isoPrepTime);
        long prepTime = duration.toMinutes();
        // Integer prepTime = this.convertISOMinute(isoPrepTime);
        this.prepTime = (int)prepTime;
    }

    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }

    public void setInstructions(List<String> instructions) { this.instructions = instructions; }

    public void findInstructions(HashMap<String, Object> recipeMap) {
        Object rInstructions = recipeMap.get("recipeInstructions");

        if (rInstructions == null) {
            return;
        } 
        System.out.println(rInstructions.getClass());

        List<HashMap<String, Object>> instrArray = (ArrayList<HashMap<String, Object>>) rInstructions;
        List<String> instructions = new ArrayList<String>();

        // for (JSONObject obj : instrArray) {
        //     Object val = obj.get("@type");
        //     String type = "";
        //     if (val != null) {type = val.toString();}

        //     if (type.equals("HowToStep")) {
        //         // Parse step
        //         String instr = obj.get("text").toString();
        //         instructions.add(instr);
        //     } 
        // }
        for (int i = 0; i < instrArray.size(); i++) {
            HashMap<String, Object> obj = instrArray.get(i);

            Object val = obj.get("@type");
            String type = "";
            if (val != null) {type = val.toString();}

            if (type.equals("HowToStep")) {
                // Parse step
                String instr = obj.get("text").toString();
                instructions.add(instr);
            } else if (type.equals("HowToSection")) {
                List<HashMap<String, Object>> itemList = (ArrayList<HashMap<String, Object>>)obj.get("itemListElement");
                for (int j = 0; j < itemList.size(); j++) {
                    HashMap<String, Object> obj_step = itemList.get(j);
                    String instr = obj_step.get("text").toString();
                    instructions.add(instr);
                }
            }
        }

        if (instructions.size() == 0) {
            System.out.println("Couldn't find instructions");
            return;
        }

        this.setInstructions(instructions);
    }

    public String convertIngredientsToString() {
        if (this.ingredients.size() == 0) {
            return "";
        }
        String ingrString = String.join(",", this.ingredients);
        return ingrString;
    }

    public void writeTextFile() {
        // TODO: Create global directory path
        this.setRecipeFile(this.recipeTitle.replaceAll(" ", "_"));
        String fileName = this.recipeFile + ".txt";
        String path = "Py_Text_Processing/input/" + fileName;
        try {
            File recipeObj = new File(path);
            if (recipeObj.createNewFile()) {
                System.out.println("File created: " + recipeObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("Could not create recipe file");
            e.printStackTrace();
        }

        StringJoiner fileText = new StringJoiner("");
        fileText.add(this.recipeTitle + "\n");
        System.out.println(this.totalTime);
        fileText.add("Total Time: " + this.totalTime.toString() + "\n\n");

        fileText.add("Ingredients: \n");
        for (String ingr: this.ingredients) {
            fileText.add(ingr + "\n");
        }
        fileText.add("\n");

        fileText.add("Instructions: \n");
        for (int i = 0; i < this.instructions.size(); i++) {
            String inst = this.instructions.get(i);
            if (i == this.instructions.size()-1) {
                fileText.add(inst);
            } else {
                fileText.add(inst + "\n");
            }
        }

        try {
            FileWriter recipeWrite = new FileWriter(path);
            recipeWrite.write(fileText.toString());
            recipeWrite.close();
        } catch (IOException e) {
            System.out.println("Could not write to recipe file");
            e.printStackTrace();
        }
    }
}
