package org.optimization;


import org.recipe_processing.Recipe;
import org.utilities.database.graph.Connection;
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

    public void createMeal(List<Recipe> recipes, List<User> buddies) {

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
        for (Recipe recipe: recipes) {
            evalSteps.add(recipe.getFinalStep()); //TODO: This assumes the first node is the final node in the recipe (MAKE SURE IT IS)
        }

        // What if we store the last node of each recipe and have a hashmap for the rest of the nodes

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
            this.mapResourceStepToUser(currStep, buddies, constraints);

            // Add all time dependent previous tasks from the current node to a new list
            List<Step> timeDependSteps = new ArrayList<Step>();

            // Iterate through time dependent tasks
            for(Connection c: currStep.getTimeDependencies()) {
                // step -> time dependent step before it

                // TODO: Check whether we need to compare this Step's id to get the Step object from the hashmap
                Step tStep = c.getEndNode();
                Integer stepTime = c.getConnectionTime();

                this.mapTimeStepToUser(tStep, stepTime);
            }

            // Add all resource dependent previous tasks from the current node to the evalNodes
            for(Connection c: currStep.getResourceDependencies()) {
                evalSteps.add(c.getEndNode());
            }


        }

        // Iterate through user stacks and set the tail node to be the head
        // send user stacks to devices this involves twilio

    }

    private void mapTimeStepToUser(
            Step s,
            Integer timeBetweenSteps,
            Integer currTime,
            List<User> buddies,
            HashMap<String, List<Integer>> constraints
    ) {
        // we could give more priority to the same user that just did the resource task that this task relies on
        System.out.println("");
    }

//    currNode = NodeE
//    time = 11
//    timeDependantNodes = [(NodeB, 10), (NodeC,15), (NodeD dependencies)]

    private void mapResourceStepToUser(
            Step s,
            List<User> buddies,
            HashMap<String, List<Integer>> constraints
    ) {
        // Check which user has the least amount of work so far
        Integer leastUserTime = buddies.get(0).getUserTime();
        User user = buddies.get(0);

        for (User u: buddies) {
            if (u.getUserTime() < leastUserTime) {
                leastUserTime = u.getUserTime();
                user = u;
            }
        }

        // Check if constraint is available at this time
        // holding holdingResource and resourcesRequired

        /*
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
         */

        Integer earliestAvailableTime = leastUserTime;


        // TODO: FINISHED HERE
        List<String> resources = s.getResourcesRequired();
        resources.add(s.getHoldingResource());
        for(String resource: resources) {
            for (Integer resourceId: constraints.get(resource)) {
                if (resourceId <= leastUserTime) {
                    break;
                }
            }
        }

        Boolean foundResource = false;




        /*
            // LOOP through current node and previous time dependent tasks
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

            // if resources are needed and found then Insert task into user
            // function should be in user object
            // update curTime based
        */
        System.out.println("");
    }
}