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
            Boolean res = MySqlConnection.addUser(user.userEmail, user.skillLevel, user.userName);

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

    @PostMapping(value = "/RequestRecipeByInput", produces="application/json")
    public ResponseEntity<Object> RequestRecipeByInput(@Valid @RequestBody RecipeInput recipeInput) {
        /**
         //once received in backend
         a) format into text file (input into text processing)
         b) parse recipe (input processing)
         c) add to graph database with id from AllRecipes
          TODO: add to FaveRecipes database for that user?

         RETURNS RECIPE INFO
         */
        try {
            // URL not parsed yet, enter input processing
            RecipeInfo recipeInfo = RecipeExtractor.parseUserRecipe(recipeInput);
            if (recipeInfo != null) {
                return ResponseEntity.ok(recipeInfo);
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("RequestRecipeByUrl Request Failed");
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
            b) add to FaveRecipes database for that user TODO: Check this happens

         RETURNS RECIPE INFO
         */
        try {
            RecipeInfo recipeInfo = MySqlConnection.doesRecipeExist(user.userEmail, user.recipeUrl);
            if (recipeInfo != null ) {
                return ResponseEntity.ok(recipeInfo);
            }

            // URL not parsed yet, enter input processing
            recipeInfo = RecipeExtractor.parseRecipeUrl(user.userEmail, user.recipeUrl);
            if (recipeInfo != null) {
                return ResponseEntity.ok(recipeInfo);
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("RequestRecipeByUrl Request Failed");
        } catch (Exception e) {
            // In case another error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/GetPastRecipes")
    public ResponseEntity<List<RecipeInfo>> GetPastRecipes(@Valid @RequestParam String userEmail) {
        try {
            List<RecipeInfo> recipes = MySqlConnection.findUserRecipes(userEmail);
            return ResponseEntity.ok(recipes);

        } catch (Exception e) {
            List<RecipeInfo> error = new ArrayList<RecipeInfo>();
            // In case another error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/RequestMealSessionSteps")
    public List<MealSessionUsersSteps> RequestMealSessionSteps(@Valid @RequestBody MealSessionInfo mealSessionInfo) {

        KitchenConstraint kitchenConstraints = mealSessionInfo.kitchenConstraints;
        List<Long> recipeIDs = mealSessionInfo.recipeIDs;
        //Should be emails
        List<String> includedFriends = mealSessionInfo.includedFriends;
        List<MealSessionUsersSteps> response = GenerateMeal.SetupMealSteps(kitchenConstraints, recipeIDs, includedFriends);

         //return meal session steps
        return response;
    }


}