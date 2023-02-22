package org.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    }

    @PostMapping("/AddKitchenConstraints")
    public void AddKitchenConstraints() {
        //get constraint and store
    }

    @PostMapping("/AddFriend")
    public void AddFriend() {
        // get friend and store
    }

    @PostMapping("/RequestRecipeByUrl")
    public String RequestRecipeByUrl() {
        //get url from api call

        //return recipe info
        return "Greetings from Spring Boot!";
    }

    @PostMapping("/RequestMealSessionSteps")
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