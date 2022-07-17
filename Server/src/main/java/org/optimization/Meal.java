package org.optimization;


import org.recipe_processing.Recipe;
import org.utilities.database.graph.Connection;
import org.utilities.database.graph.Step;
import scala.Int;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.Math;

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

            Integer currStepId = 0;
            for (Integer i = 0; i < evalSteps.size(); i++) {
                Step s = evalSteps.get(i);
                if (s.getTimeLeft() > maxTimeLeft) {
                    maxTimeLeft = s.getTimeLeft();
                    currStepId = i;
                }
            }

            Step currStep = evalSteps.get(currStepId);

            /*
                // LOOP through current node and previous time dependent tasks
                // Compare Time Dependant Nodes against current time
                // If they need to get scheduled at this time that happens now
                // This ensures they get priority
                // Does this need monitoring
                    // if so does it have someone to watch it
                        // Find user who has been available the longest
                        // Check all user time counters, the one with the least is selected
                // For init protoype, assume only elements, oven, big applicances for resources with infinite pans
            */

            // Helper function -> assign Step to User
            Integer priorIdx = this.mapResourceStepToUser(currStep, buddies, constraints);

            // Add all time dependent previous tasks from the current node to a new list
            List<Step> timeDependSteps = new ArrayList<Step>();

            // Iterate through time dependent tasks
            for(Connection c: currStep.getTimeDependencies()) {
                // step -> time dependent step before it

                // TODO: Check whether we need to compare this Step's id to get the Step object from the hashmap
                Step tStep = c.getEndNode();
                // TODO: Do we need to use stepTime?
                Integer stepTime = c.getConnectionTime();

                this.mapTimeStepToUser(tStep, priorIdx, buddies, constraints);
            }

            // Add all resource dependent previous tasks from the current node to the evalNodes
            for(Connection c: currStep.getResourceDependencies()) {
                evalSteps.add(c.getEndNode());
            }
        }

        // Iterate through user stacks and set the tail node to be the head
        // send user stacks to devices this involves twilio

        // tail -> prev -> prev -> ... -> head

    }

    private void mapTimeStepToUser(
            Step s,
            Integer prioBuddyIdx,
            List<User> buddies,
            HashMap<String, List<Integer>> constraints
    ) {
        // we could give more priority to the same user that just did the resource task that this task relies on
        // check if prioritized buddy can take this process first
        User prioBuddy = buddies.get(prioBuddyIdx);
        Integer currentTime = prioBuddy.getCurrentTime();

        Integer taskStartTime = currentTime + prioBuddy.getRecentTask().getStep().getStepTime();

        // Confirm task within constraints
        // Constraints: User does not have another task at the same time already booked
        UserTask traverse = prioBuddy.getRecentTask();
        Boolean taskFits = false;
        while (traverse != null) {
            Integer after = traverse.getStartTime() + traverse.getUserTime();

            if (after < taskStartTime && (traverse.getNext() == null || taskStartTime + s.getStepTime() <= traverse.getNext().getStartTime())) {
                // we're good
                taskFits = true;
                break;
            } else {
                traverse = traverse.getNext();
            }

        }

        if (!taskFits) {
            // TODO: Find another user to do the task
        }

        // If it passes the constraints
        List<String> resources = s.getResourcesRequired();
        resources.add(s.getHoldingResource());

        // TODO: Booking resources in advance breaks our constraint implementation
        // We might need to use a different data structure to represent each resource
        Integer taskStart = findTimeToGetConstraints(taskStartTime, s, resources, constraints);

        if (taskStartTime < taskStart) {
            // TODO: what do we do in this situation?
            // we have to wait for resources to become available.
            // we want to have our time dependent task scheduled immediately before the current task
            // if the resources need more time then do we want to push our time dependent task back?
        }

        // Insert into user stack
        // TODO: Change last parameter to userTime for a task
        UserTask newTask = new UserTask(s, taskStart, taskStart);

        // traverse task will always be before newTask
        if (traverse.getNext() != null) {
            // Insert newTask between recent and its old next task
            UserTask temp = traverse.getNext();
            temp.setPrev(newTask);
            newTask.setNext(temp);
            newTask.setPrev(traverse);
            traverse.setNext(newTask);

        } else {
            traverse.setNext(newTask);
            newTask.setPrev(traverse);
        }
        // TODO: Add user time instead of step time
        prioBuddy.setAllottedTime(prioBuddy.getAllottedTime() + s.getStepTime());
    }

//    currNode = NodeE
//    time = 11
//    timeDependantNodes = [(NodeB, 10), (NodeC,15), (NodeD dependencies)]

    private Integer mapResourceStepToUser(
            Step s,
            List<User> buddies,
            HashMap<String, List<Integer>> constraints
    ) {
        // Check which user has the least amount of work so far
        Integer leastUserTime = buddies.get(0).getCurrentTime();
        User user = buddies.get(0);
        Integer userIdx = 0;

        // TODO: Change this process to a min heap?

        for (User u: buddies) {
            if (u.getCurrentTime() < leastUserTime) {
                leastUserTime = u.getCurrentTime();
                user = u;
                userIdx = 0;
            }
        }

        // Check if constraint is available at this time
        // holding holdingResource and resourcesRequired
        List<String> resources = s.getResourcesRequired();
        resources.add(s.getHoldingResource());

        Integer taskStart = findTimeToGetConstraints(leastUserTime, s, resources, constraints);

        // TODO: Change last parameter to userTime for a task
        appendTaskToUser(user, s, taskStart, taskStart);

        // Return user so that we may be able to prioritize this user to work on time dependent tasks
        return userIdx;
    }

    private Integer findLeastTimeUser() {
        // TODO: Change user list to a min heap. Return the lowest value in the heap
        return 0;
    }

    private Integer findTimeToGetConstraints(
            Integer leastUserTime,
            Step s,
            List<String> resources,
            HashMap<String, List<Integer>> constraints) {
        List<Integer> resourceIds = new ArrayList<Integer>();
        List<Integer> earliestTimes = new ArrayList<Integer>();

        // Option 1: Find user with least amount of work then find the constraints that satisfy

        // this data structure will probably not work?
        // if we have a time dependency that we are planning to use a resource in the future, how do we identify when the resource is free beforehand?
        // if we assume time dependent constraints do not require resources than it's fine...but this is a bad assumption
        for(String resource: resources) {
            // Check list of elements for a resource and see if any are less than leastUserTime
            // If not, store index of resource with lowest time
            Integer optimalIdx = 0;

            // TODO: Change value of earliestAvailResource
            Integer earliestAvailResource = 100000;
            Integer closestDiff = 10000;
            Boolean foundResource = false;

            Integer i = 0;
            for (Integer rTime: constraints.get(resource)) {
                Integer diff = leastUserTime - rTime;
                if (0 < diff && Math.abs(diff) < closestDiff) {
                    closestDiff = Math.abs(diff);
                    optimalIdx = i;
                    // Update constraint time and exit
                    foundResource = true;
                } else if (!foundResource && Math.abs(diff) < closestDiff){
                    closestDiff = Math.abs(diff);
                    optimalIdx = i;
                }
                i++;
            }

            resourceIds.add(optimalIdx);
            Integer earlyTime = constraints.get(resource).get(optimalIdx);
            earliestTimes.add(earlyTime);
        }

        Integer taskStart = Collections.min(earliestTimes);
        // Update the earliest accessible time for each resource used
        for(Integer i = 0; i < resources.size(); i++) {
            constraints.get(resources.get(i)).set(resourceIds.get(i), taskStart + s.getStepTime());
        }

        // Find earliest time
        return taskStart;
    }

    private void appendTaskToUser(User user, Step s, Integer taskStart, Integer userTime) {
        // TODO: Change second parameter to userTime for a task
        UserTask newTask = new UserTask(s, taskStart, userTime);

        // if resources are needed and found then Insert task into user
        if (user.getAllottedTime() == 0) {
            user.setHead(newTask);
            user.setRecentTask(newTask);
            // TODO: Update the tail

            // TODO: Add user time instead of step time
            user.setAllottedTime(user.getAllottedTime() + userTime);
            user.setCurrentTime(user.getCurrentTime() + s.getStepTime());

        } else {
            UserTask recent = user.getRecentTask();

            // TODO: Add current time math

            // Check if there's enough space to add task
            if (recent.getNext() != null && (user.getCurrentTime() + taskStart > recent.getNext().startTime)) {
                // if no space, find other users at that time??
            }

            // there's enough space to insert
            // Insert newTask between recent and its old next task
            UserTask temp = recent.getNext();
            temp.setPrev(newTask);
            newTask.setNext(temp);
            newTask.setPrev(recent);
            recent.setNext(newTask);

            user.setRecentTask(newTask);

            // Update counters
            // TODO: Add user time instead of step time
            user.setAllottedTime(user.getAllottedTime() + s.getStepTime());
            user.setCurrentTime(user.getCurrentTime() + s.getStepTime());
        }
    }
}
