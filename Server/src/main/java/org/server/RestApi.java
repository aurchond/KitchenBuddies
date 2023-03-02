package org.server;

import org.input_processing.RecipeExtractor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.utilities.database.relational.MySqlConnection;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RestApi {
    private String successMsg = "Success";
    private String failureMsg = "Failure";

    @PostMapping("/AddUser")
    public ResponseEntity<Object> AddUser(@Valid @RequestBody ApiUser user) {
        try {
            Boolean res = MySqlConnection.addUser(user.userEmail, user.skillLevel, user.username);

            if (res) {
                return ResponseEntity.ok(successMsg);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("AddUser Request Failed");

        } catch (Exception e) {
            // In case another error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/GetUserSkill")
    public ResponseEntity<Object> GetUserSkill(@Valid @RequestParam String userEmail) {
        try {
            int skillLvl = MySqlConnection.getSkillLevel(userEmail);
            return ResponseEntity.ok(skillLvl);

        } catch (Exception e) {
            // In case another error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/AddSkillLevel")
    public ResponseEntity<Object> AddSkillLevel(@Valid @RequestBody ApiUser user) {
        try {
            Boolean res = MySqlConnection.addSkillLevel(user.userEmail, user.skillLevel);
            if (res) {
                return ResponseEntity.ok(successMsg);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("AddSkillLevel Request Failed");
        } catch (Exception e) {
            // In case another error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/GetKitchenConstraints")
    public ResponseEntity<Object> GetKitchenConstraints(@Valid @RequestParam String userEmail) {
        try {
            KitchenConstraint userConstraints = MySqlConnection.getKitchen(userEmail);
            return ResponseEntity.ok(userConstraints);
        } catch (Exception e) {
            // In case another error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/AddKitchenConstraints")
    public ResponseEntity<Object> AddKitchenConstraints(@Valid @RequestBody KitchenConstraint kConstraint) {
        try {
            Boolean res = MySqlConnection.addKitchen(kConstraint.userEmail, kConstraint.burner, kConstraint.pan,
                                    kConstraint.pot, kConstraint.knife, kConstraint.cuttingBoard, kConstraint.oven, kConstraint.microwave);
            if (res) {
                return ResponseEntity.ok(successMsg);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("AddKitchenConstraints Request Failed");
        } catch (Exception e) {
            // In case another error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/GetFriendsList")
    public ResponseEntity<Object> GetFriendsList(@Valid @RequestParam String userEmail) {
        try {
            List<String> friends = MySqlConnection.getFriendsList(userEmail);
            Friends f = new Friends(friends);
            return ResponseEntity.ok(f);
        } catch (Exception e) {
            // In case another error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/AddFriend")
    public ResponseEntity<Object> AddFriend(@Valid @RequestBody ApiUser user) {
        try {
            Boolean res = MySqlConnection.addToFriendsList(user.userEmail, user.newFriend);
            if (res) {
                String ret_val =  user.userEmail + " and " + user.newFriend + " are now friends!";
                return ResponseEntity.ok(ret_val);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("AddKitchenConstraints Request Failed");

        } catch (Exception e) {
            // In case another error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping(value = "/RequestRecipeByUrl", produces="application/json")
    public ResponseEntity<Object> RequestRecipeByUrl(@Valid @RequestBody ApiUser user) {
        /**
        //once received in backend
            a) backend checks url in AllRecipes database
                i) if recipe does not exist, add to AllRecipes database to get auto ID
                i.i) parse recipe (input processing)
                i.ii) add to graph database with id from AllRecipes
            b) add to FaveRecipes database for that user
         */
        try {
            Boolean res = MySqlConnection.doesRecipeExist(user.userEmail, user.recipeUrl);
            if (res) {
                return ResponseEntity.ok("Recipe already in both databases");
            }

            // URL not parsed yet, enter input processing
            res = RecipeExtractor.parseRecipeUrl(user.userEmail, user.recipeUrl);
            if (res) {
                return ResponseEntity.ok("Recipe added to user " + user.userEmail);
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("RequestRecipeByUrl Request Failed");
        } catch (Exception e) {
            // In case another error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/GetPastRecipes")
    public ResponseEntity<List<PastRecipe>> GetPastRecipes(@Valid @RequestParam String userEmail) {
        try {
            List<PastRecipe> recipes = MySqlConnection.findUserRecipes(userEmail);
            return ResponseEntity.ok(recipes);

        } catch (Exception e) {
            List<PastRecipe> error = new ArrayList<PastRecipe>();
            // In case another error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/RequestStepsByID")
    public String RequestStepsByID(@Valid @RequestBody MealSessionInfo mealSessionInfo) {
        // System.out.println(mealSessionInfo.kitchenConstraints);
        // for (Integer id : mealSessionInfo.recipeIDs) {
        //     System.out.println(id);
        // }
        // TODO: Refactor SetupMealSteps to output a JSONString (or an object in the same structure)
        GenerateMeal.SetupMealSteps();

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


}
