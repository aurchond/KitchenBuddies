package org.input_processing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

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
        Integer totalTime = this.convertISOMinute(isoTotalTime);
        this.totalTime = totalTime;
    }

    public void setCookTime(String isoCookTime) {
        Integer cookTime = this.convertISOMinute(isoCookTime);
        this.cookTime = cookTime;
    }

    public void setPrepTime(String isoPrepTime) {
        Integer prepTime = this.convertISOMinute(isoPrepTime);
        this.prepTime = prepTime;
    }

    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }

    public void setInstructions(List<String> instructions) { this.instructions = instructions; }

    private Integer convertISOMinute(String isoTime) {
        String minute = isoTime.substring(2, isoTime.length()-1);
        System.out.println(minute);
        System.out.println(Integer.parseInt(minute));
        return Integer.parseInt(minute);
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
        for (String inst: this.instructions) {
            fileText.add(inst + "\n");
        }
        fileText.add("\n");

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
