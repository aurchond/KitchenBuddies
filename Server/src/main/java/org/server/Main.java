package org.server;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // For debugging
        // Setup Constraints, buddies, and recipeIDs

        KitchenConstraint kc = new KitchenConstraint();
        kc.userEmail = "maya@gmail.com";
        kc.pot = 1;
        kc.pan = 2;
        kc.bowl = 3;
        kc.cuttingBoard = 2;
        kc.oven = 1;

        List<String> buddies = new ArrayList<String>();
        buddies.add("caleb@gmail.com");
        buddies.add("marley@gmail.com");

        List<Long> demoRecipes = new ArrayList<Long>();
        demoRecipes.add(200L); // Greek Pasta Salad
        demoRecipes.add(101L); // Schnitzel
        demoRecipes.add(102L); // Broccoli
        // demoRecipes.add(103L);

        GenerateMeal gm = new GenerateMeal();
        gm.SetupMealSteps(kc, demoRecipes, buddies);
        //gm.SetupMealSteps(kitchenConstraints, recipeIDs, includedFriends);
    }
}