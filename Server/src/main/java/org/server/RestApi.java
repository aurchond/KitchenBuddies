package org.server;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
public class RestApi {

    @GetMapping("/GetUserSkill")
    public String GetUserSkill() {
        //return skill
        return "Greetings from Spring Boot!";
    }

    @PostMapping("/AddSkillLevel")
    public void AddSkillLevel() {
        //get value and set
    }

    @GetMapping("/GetConstraintsAndFriends")
    public String GetConstraintsAndFriends() {
        //return constraints and skills
        return "Greetings from Spring Boot!";
        /**THESE ARE 2 SEPARATE JSON ARE THEY GETTING COMBINED?
         *
         * {
         *     "userEmail": "shadisz@yahoo.ca",
         *     "skill": 2,
         *     "burner": 3,
         *     "pot": 4,
         *     "pan": 2,
         *     "knife": 5,
         *     "cuttingBoard": 1,
         *     "oven": 1,
         *     "microwave": 2
         * }
         *
         *
         *
         * {
         *     "friends":
         *     [
         *         "aditi@gmail.com",
         *         "shadi@gmail.com",
         *         "aurchon@gmail.com",
         *         "branden@gmail.com"
         *     ]
         * }
         */
    }

    @PostMapping("/AddKitchenConstraints")
    public void AddKitchenConstraints() {
        //get constraint and store
    }

    @PostMapping("/AddFriend")
    public void AddFriend() {
        // get friend and store
        /**
         {
         "userEmail": "shadisz@yahoo.ca",
         "newFriend": "aurchond@gmail.com"
         }
         */
    }

    @PostMapping(value = "/RequestRecipeByUrl", produces="application/json")
    @ResponseBody
    public String RequestRecipeByUrl(@Valid @RequestBody RecipeInfo recipeInfo) {
        //get url from api call


        //return recipe info
        return "Greetings from Spring Boot!";
    }

    @PostMapping("/RequestMealSessionSteps")
    @ResponseBody
    public String RequestMealSessionSteps(@Valid @RequestBody MealSessionInfo mealSessionInfo) {
        // get constraints
        // get friends
        // get recipes
        /**
         * {
             "kitchenConstraints": {
                 "userEmail": "shadisz@yahoo.ca",
                 "skill": 2,
                 "burner": 3,
                 "pot": 4,
                 "pan": 2,
                 "knife": 5,
                 "cuttingBoard": 1,
                 "oven": 1,
                 "microwave": 2
             },
             "recipeIDs": [
                 2, 4, 6
             ],
             "includedFriends": [
                 "aditi@gmail.com",
                 "shadi@gmail.com"
             ]
         }
        */

         //return meal session steps
        return "Greetings from Spring Boot!";
        /**
         * Responses
         * [
         *     {
         *         "userEmail": "aurchond@gmail.com",
         *         "recipeStep": [
         *             {
         *                 "number": "2.1",
         *                 "instructions": "heat oil in a wok",
         *                 "ingredientList": [
         *                     "teaspoon coconut oil"
         *                 ],
         *                 "ingredientQuantity": [
         *                     1
         *                 ],
         *                 "dependencyItem": "",
         *                 "nextUserEmail": "aurchond@gmail.com"
         *             },
         *             {
         *                 "number": "2.2",
         *                 "instructions": "cook veggies in wok",
         *                 "ingredientList": [
         *                     "teaspoon salt",
         *                     "shredded carrots"
         *                 ],
         *                 "ingredientQuantity": [
         *                     1,
         *                     2
         *                 ],
         *                 "dependencyItem": "",
         *                 "nextUserEmail": "shadi@gmail.com"
         *             }
         *         ]
         *     }
         * ]
         */
    }

    @PostMapping("/AddRecipes")
    public void AddRecipes() {
        //get recipes and store them to user
        /**
         * {
            "userEmail": "shadisz@yahoo.ca",
                "recipeUrl": "www.allrecipes.com/fish"
        }

        //once received in backend
            a) backend checks url in AllRecipes database
                i) if recipe does not exist, add to AllRecipes database to get auto ID
                i.i) parse recipe (input processing)
                i.ii) add to graph database with id from AllRecipes
            b) add to FaveRecipes database for that user
         */
    }

    @GetMapping("/GetPastRecipes")
    public List<PastRecipe> GetPastRecipes(@Valid @RequestBody UserInfo userInfo) {
        //get past recipes for user
        String[] ingredientList1 = {"1 cup of sugar", "2 carrots"};
        String[] ingredientList2 = {"1 chicken", "cup of parmesan"};
        PastRecipe[] pastRecipesArray = {new PastRecipe("sugar carrots", Arrays.asList( ingredientList1), 1.5F, "22-3-14"), new PastRecipe("chicken parm",  Arrays.asList(ingredientList2), 1F, "22-2-10")};
        return Arrays.asList(pastRecipesArray);
        /**
         *
         * [{
         *     "recipeName": "sugar carrots",
         *     "ingredientList": [
         *         "1 cup of sugar",
         *         "2 carrots"
         *     ],
         *     "completionTime": 1.5,
         *     "lastTimeMade": "22-3-14"
         * },
         * {
         *     "recipeName": "chicken parm",
         *     "ingredientList": [
         *         "1 chicken",
         *         "cup of parmesan"
         *     ],
         *     "completionTime": 1,
         *     "lastTimeMade": "22-2-10"
         * }
         * ]
         *
         */
    }


}