package org.server;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.input_processing.RecipeExtractor.parseRecipeUrl;

import javax.validation.Valid;

@RestController
public class RestApi {

    @GetMapping("/GetUserSkill")
    public String GetUserSkill(@RequestBody User user) {
        //return skill
        String ret = "Greetings from Spring Boot! " + user.userEmail;
        return ret;
    }

    @PostMapping("/AddSkillLevel")
    public void AddSkillLevel(@RequestBody User user) {
        //get value and set
    }

    @GetMapping("/GetConstraintsAndFriends")
    public String GetConstraintsAndFriends(@RequestBody User user) {
        //return constraints and skills
        String ret = "Greetings from Spring Boot! " + user.userEmail;
        return ret;
    }

    @PostMapping("/AddKitchenConstraints")
    public void AddKitchenConstraints(@RequestBody User user) {
        //get constraint and store
    }

    @PostMapping("/AddFriend")
    public void AddFriend(@RequestBody User user) {
        // get friend and store
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
    public String GetPastRecipes() {
        //get past recipes for user
        return "Greetings from Spring Boot!";
    }


}