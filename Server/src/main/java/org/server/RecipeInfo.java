package org.server;

import java.util.List;

public class RecipeInfo {
    public Long recipeID;
    public String recipeName;
    public List<String> ingredientList;
    public Integer totalTimeMinutes;
    public String lastTimeMade;//might switch to Date

    public RecipeInfo(Long recipeID, String recipeName, List<String> ingredientList, Integer completionTime, String lastTimeMade) {
        this.recipeID = recipeID;
        this.recipeName = recipeName;
        this.ingredientList = ingredientList;
        this.totalTimeMinutes = completionTime;
        this.lastTimeMade = lastTimeMade;
    }
}
