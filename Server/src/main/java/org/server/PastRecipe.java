package org.server;

import java.util.List;

public class PastRecipe {
    String recipeName;
    List<String> ingredientList;
    Float completionTime;
    String lastTimeMade;//might switch to Date

    public PastRecipe(String recipeName,List<String> ingredientList,Float completionTime,String lastTimeMade) {
        this.recipeName = recipeName;
        this.ingredientList = ingredientList;
        this.completionTime = completionTime;
        this.lastTimeMade = lastTimeMade;
    }
}
