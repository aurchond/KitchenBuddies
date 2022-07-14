package org.recipe_processing;

import org.utilities.database.graph.Step;

import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
    public static List<Step>  createRecipe(
            List<Step> Steps,
            HashMap<String, List<Integer>> ingredients,
            HashMap<String, List<Integer>> toolsRequired,
            HashMap<String, List<Integer>> holdingResource_Id
    ){
        return Steps;//TODO Fill in with recipe processing
    }
}