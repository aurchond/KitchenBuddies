package org.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.utilities.database.relational.MySqlConnection;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
public class RestApi {

    @PostMapping("/AddUser")
    public ResponseEntity<Object> AddUser(@Valid @RequestBody User user) {
        try {
            Boolean res = MySqlConnection.addUser(user.userEmail, user.skillLevel, user.username);

            if (res) {
                return ResponseEntity.ok("Success!");
            } 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("AddUser Request Failed");

        } catch (Exception e) {
            // In case another error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/GetUserSkill")
    public String GetUserSkill(@Valid @RequestBody User user) {
        // System.out.println(user.userEmail);
        int skillLvl = MySqlConnection.getSkillLevel(user.userEmail);

        // TODO: Return proper type
        // TODO: Add error checking?
        return "Greetings from Spring Boot! " + Integer.toString(skillLvl);
    }

    @PostMapping("/AddSkillLevel")
    public String AddSkillLevel(@Valid @RequestBody User user) {
        Boolean res = MySqlConnection.addSkillLevel(user.userEmail, user.skillLevel);
    
        // TODO: Return proper type Success or failure?
        String ret_val = "SkillLevel update of " + Integer.toString(user.skillLevel) + " for " + user.userEmail;

        if (res) {
            ret_val +=  " succeeded!";
        } else {
            ret_val +=  " failed";
        }
        
        // TODO: Return proper type
        return ret_val;
    }

    @GetMapping("/GetConstraintsAndFriends")
    public String GetConstraintsAndFriends(@Valid @RequestBody User user) {
        KitchenConstraint userConstraints = MySqlConnection.getKitchen(user.userEmail);
        List<String> userFriends = MySqlConnection.getFriendsList(user.userEmail);

        // TODO: Combine KitchenConstraint object and friend list into 1 json
        System.out.println(userConstraints.toString());
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
    public String AddKitchenConstraints(@Valid @RequestBody KitchenConstraint kConstraint) {
        //TODO: Do we need to confirm if all input values are valid? Is this done on the front end? (i.e. number of ovens isn't defined)
        Boolean res = MySqlConnection.addKitchen(kConstraint.userEmail, kConstraint.burner, kConstraint.pan, 
                                 kConstraint.pot, kConstraint.knife, kConstraint.cuttingBoard, kConstraint.oven, kConstraint.microwave);

        // TODO: Return proper type Success or failure?
        String ret_val = "KitchenConstraints update for " + kConstraint.userEmail;

        if (res) {
            ret_val +=  " succeeded!";
        } else {
            ret_val +=  " failed";
        }

        return ret_val;
    }

    @PostMapping("/AddFriend")
    public String AddFriend(@Valid @RequestBody User user) {
        /**
         {
         "userEmail": "shadisz@yahoo.ca",
         "newFriend": "aurchond@gmail.com"
         }
        */
        // TODO: Is adding friends bidirectional?
        Boolean res = MySqlConnection.addToFriendsList(user.userEmail, user.newFriend);

        // TODO: Return proper type Success or failure?
        String ret_val = "";
        if (res) {
            ret_val +=  user.userEmail + " and " + user.newFriend + " are now friends!";
        } else {
            ret_val +=  "Friend update for " + user.userEmail + " failed";
        }

        return ret_val;
    }

    @PostMapping(value = "/RequestRecipeByUrl", produces="application/json")
    @ResponseBody
    public String RequestRecipeByUrl(@Valid @RequestBody RecipeInfo recipeInfo) {
        //get url from api call
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

    @GetMapping("/GetPastRecipes")
    public List<PastRecipe> GetPastRecipes(@Valid @RequestBody User user) {
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