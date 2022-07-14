package org.optimization;


import org.utilities.database.graph.Step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.utilities.database.relational.Main.relationDbFunctionGetConstraints;

public class Meal {
    // Hashmap of Kitchen Constraints (key=string, value=list<int>(each element is time the resource is next available))
    // List of Task Stacks (each element is a Task Class)


    public Meal () {

    }


    public void createMeal(List<List<Step>> recipes, List<User> buddies) {

        /* Objects to pass in
            - List of Recipe keys to access database
            - List of User objects of those working in kitchen
                - First user is the user who initiated the meal
        */

        // First user is the user who initiated the meal
        User p_bud = buddies.get(0);

        // Get User kitchen constraints from database (Helper Method within Optimization?)
        // TODO: For prototype- Hardcode Constraints
        HashMap<String, List<Integer>> constraints = relationDbFunctionGetConstraints(p_bud);

        /* Initialize local variables
            - maxTimeLeft = 0 (Used to identify task from recipe with largest TimeLeft)
            - Step currStep =
            - List<Nodes> evalNodes =  (stores the nodes of each recipe we are currently analyzing)
            - curTime = 0 (represents the current time for our current task
         */

        // Identifies task from recipe with largest TimeLeft
        Integer maxTimeLeft = 0;
        // Time of current task
        Integer time = 0;

        // Current steps being evaluated
        List<Step> evalSteps = new ArrayList<Step>();

        // Initialize evalSteps list - get the last step in each recipe
        for (List<Step> recipe: recipes) {
            evalSteps.add(recipe.get(0)); //TODO: This assumes the first node is the final node in the recipe (MAKE SURE IT IS)
        }

        //TODO: Take this out if first node is final recipe step
        // Find end node of each recipe
        // Instead of for loop to traverse, we could query the database to get the head node
        // Should we just add a Step property called isLastStep and do a query?
        /*  for each recipe
                for recipe.node
                    if recipe.node.next is null
                        evalNodes.add(recipe.node)
        */

        while (evalSteps.size() > 0) {
            // Find step to assign to user based on timeLeft

            /*
                for i in evalNodes
                    if evalNodes[i].timeLeft > maxTimeLeft
                        maxTimeLeft = evalNodes[i].timeLeft
                        curNodeId = i
                // Pop most urgent task off array
                curNode = evalNodes.pop(curNodeId)
                // List<Nodes> tDependentNodes

                for prev in curNode->prevTask
                    if prev relation with curNode == timeDependent
                        tDependentNodes.add(prev)
                    else
                        evalNodes.add(prev)
             */
            Integer currStepId = 0;
            for (Integer i = 0; i < evalSteps.size(); i++) {
                Step s = evalSteps.get(i);
                if (s.getTimeLeft() > maxTimeLeft) {
                    maxTimeLeft = s.getTimeLeft();
                    currStepId = i;
                }
            }

            Step currStep = evalSteps.get(currStepId);

            // Helper function -> assign Step to User
            this.mapResourceStepToUser(currStep);

            // Add all time dependent previous tasks from the current node to a new list
            List<Step> timeDependSteps = new ArrayList<Step>();

            // Iterate through time dependent tasks
            for(Step s: currStep.getTimeDependencies()) {
                this.mapTimeStepToUser(s);
            }

            // Add all resource dependent previous tasks from the current node to the evalNodes
            for(Step s: currStep.getResourceDependencies()) {
                evalSteps.add(s);
            }


        }

        // Iterate through user stacks and set the tail node to be the head
        // send user stacks to devices this involves twilio

    }

    private void mapTimeStepToUser(Step s) {
        System.out.println("");
    }

    private void mapResourceStepToUser(Step s) {
        /*
            // LOOP through current node and it's previous time dependent tasks
            // Compare Time Dependant Nodes against current time
            // If they need to get scheduled at this time that happens now
            // This ensures they get priority
            // Does this need monitoring
                // if so does it have someone to watch it
                    // Find user who has been available the longest
                    // Check all user time counters, the one with the least is selected
            curTime = User.timeCtr
            // For init protoype, assume only elements, oven, big applicances for resources with infinite pans
            // Does this node need a resource from the resource list
            //If so find the earliest time this can happen
            // Extract/update data from resource hashmap
            earliestAvailableTime = inf
            boolean foundResource = False
            for resourceTime in meal.resources(curNode.holdingResource)
                if resourceTime <= curTime
                    resourceTime += node.taskTime
                    foundResource = True
                    break
                else if resourceTime < earliestAvailableTime
                    earliestAvailableTime = resourceTime
            if not foundResource
                curTime = earliestAvailableTime
            // if resources are needed and found then Insert task into user
            // function should be in user object
            // update curTime based
        */
        System.out.println("");
    }
}