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
    @ResponseBody
    public ResponseEntity<Object> RequestRecipeByUrl(@Valid @RequestBody User user) {
        // get url from api call
        // return recipe info
        // String ret = "Greetings from Spring Boot! " + user.userEmail;
        //parseRecipeUrl(url)
        Boolean result = parseRecipeUrl(user.recipeURL);

        if (!result) {
            return ResponseEntity.ok("Error in parsing Recipe URL");
        }

        return ResponseEntity.ok(user.recipeURL);
    }

    @PostMapping("/RequestMealSessionSteps")
    @ResponseBody
    public String RequestMealSessionSteps() {
        // get constraints
        // get friends
        // get recipes

        //return meal session steps
        return "Greetings from Spring Boot!";
    }

    @PostMapping("/AddRecipes")
    public void AddRecipes() {
        //get recipes and store them to user
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
    }


}
