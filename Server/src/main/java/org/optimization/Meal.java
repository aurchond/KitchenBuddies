package org.optimization;


import org.recipe_processing.Recipe;
import org.utilities.database.graph.Connection;
import org.utilities.database.graph.Step;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.utilities.database.relational.Main.relationDbFunctionGetConstraints;

public class Meal {
    // Hashmap of Kitchen Constraints (key=string, value=list<int>(each element is time the resource is next available))
    // List of Task Stacks (each element is a Task Class)
    HashMap<String, HashMap<Integer, Integer>> resourceMap = new HashMap<String, HashMap<Integer, Integer>>();

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
        HashMap<String, List<Resource>> constraints = relationDbFunctionGetConstraints(p_bud);

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
            evalSteps.add(recipe.getFinalStep());
        }

        // What if we store the last node of each recipe and have a hashmap for the rest of the nodes

        while (evalSteps.size() > 0) {
            // Find step to assign to user based on timeLeft

            int currStepId = 0;
            for (int i = 0; i < evalSteps.size(); i++) {
                Step s = evalSteps.get(i);
                if (s.getTimeLeft() > maxTimeLeft) {
                    maxTimeLeft = s.getTimeLeft();
                    currStepId = i;
                }
            }

            maxTimeLeft = 0;

            // TODO: Manually pop value
            Step currStep = null;

            for (int i = 0; i < evalSteps.size(); i++) {
                currStep = evalSteps.get(i);
                if (i == currStepId) {
                    evalSteps.remove(currStep);
                    break;
                }
            }

            System.out.println("Working on " + currStep.getNodeID());

            if (currStep.getNodeID() == 120.3 || currStep.getNodeID() == 122.1) {
                System.out.println("Debugging");
            }

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
            int priorIdx = this.mapResourceStepToUser(currStep, buddies, constraints);

            // Add all time dependent previous tasks from the current node to a new list
            List<Step> timeDependSteps = new ArrayList<Step>();

            // Iterate through time dependent tasks
//            for(Connection c: currStep.getTimeDependencies()) {
//                // step -> time dependent step before it
//
//                // TODO: Check whether we need to compare this Step's id to get the Step object from the hashmap
//                Step tStep = c.getEndNode();
//                // TODO: Do we need to use stepTime?
//                int stepTime = c.getConnectionTime();
//
//                this.mapTimeStepToUser(tStep, priorIdx, buddies, constraints);
//            }

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
            HashMap<String, List<Resource>> constraints
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
        List<Object> taskResources = findTimeToGetConstraints(taskStartTime, s, "", resources, constraints);

        if (taskStartTime < (Integer) taskResources.get(0)) {
            // TODO: what do we do in this situation?
            // we have to wait for resources to become available.
            // we want to have our time dependent task scheduled immediately before the current task
            // if the resoures need more time then do we want to push our time dependent task back?
        }

        // Insert into user stack
        // TODO: Change last parameter to userTime for a task
        UserTask newTask = new UserTask(s, (Integer)taskResources.get(0), (Integer)taskResources.get(0));

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
            HashMap<String, List<Resource>> constraints
    ) {
        // Check which user has the least amount of work so
        UserTask recent = buddies.get(0).getRecent();



        Integer leastUserTime = (recent != null) ? recent.getStartTime() + recent.getUserTime() : 0;
        User user = buddies.get(0);
        Integer userIdx = 0;

        // TODO: Change this process to a min heap?

        for (User u: buddies) {
            recent = u.getRecent();
            int recentEndTime = (recent != null) ? recent.getStartTime() + recent.getUserTime() : 0;

            if (recentEndTime < leastUserTime) {
                leastUserTime = recentEndTime;
                user = u;
                userIdx = 0;//TODO: this should be set to an index
            }
        }

        // Check if constraint is available at this time
        // holding holdingResource and resourcesRequired
        List<String> resources = s.getResourcesRequired();
        String holdingResource = s.getHoldingResource() != null ? s.getHoldingResource() : "";

        List<Object> taskResources = findTimeToGetConstraints(leastUserTime, s, holdingResource, resources, constraints);

        if (s.getHoldingResource() != null) {resources.add(0, holdingResource); }

        if(appendResourceTaskToUser(user, s, (Integer) taskResources.get(0), s.getUserTime())) {
            updateResourceTimes(s, resources, constraints, (List<Integer>) taskResources.get(1), (Integer) taskResources.get(0));
        }
        // Return user so that we may be able to prioritize this user to work on time dependent tasks
        return userIdx;
    }

    private Integer findLeastTimeUser() {
        // TODO: Change user list to a min heap. Return the lowest value in the heap
        return 0;
    }

    private List<Object> findTimeToGetConstraints(
            Integer leastUserTime,
            Step s,
            String hResource,
            List<String> tools,
            HashMap<String, List<Resource>> constraints) {
        List<Integer> resourceIds = new ArrayList<Integer>();
        List<Integer> earliestTimes = new ArrayList<Integer>();
        Integer holdingResource = -1;

        // Holding Resource first
        if(resourceMap.get(hResource) != null) {
            holdingResource = resourceMap.get(hResource).get(s.getHoldingID());
            Resource holdResource = constraints.get(hResource).get(holdingResource);
            earliestTimes.add(holdResource.getTimeAvailable());
            resourceIds.add(holdingResource);
        } else {

            Integer optimalIdx = 0;

            Integer earliestAvailResource = Integer.MAX_VALUE;
            Integer closestDiff = Integer.MAX_VALUE;
            Boolean foundResource = false;

            Integer i = 0;
            for (Resource holdResource : constraints.get(hResource)) {
                Integer rTime = holdResource.getTimeAvailable();
                Integer diff = leastUserTime - rTime;
                if (0 <= diff && Math.abs(diff) < closestDiff) {
                    closestDiff = Math.abs(diff);
                    optimalIdx = i;
                    // Update constraint time and exit
                    foundResource = true;
                } else if (!foundResource && Math.abs(diff) < closestDiff) {
                    closestDiff = Math.abs(diff);
                    optimalIdx = i;
                }
                i++;
            }

            resourceIds.add(optimalIdx);
            HashMap<Integer, Integer> resourceIdMapping = new HashMap<Integer, Integer>();
            resourceIdMapping.put(s.getHoldingID(), optimalIdx);
            resourceMap.put(hResource, resourceIdMapping);

            Resource holdResource = constraints.get(hResource).get(optimalIdx);
            earliestTimes.add(holdResource.getTimeAvailable());
        }


        for(String resource: tools) {
            // Check list of elements for a resource and see if any are less than leastUserTime
            // If not, store index of resource with lowest time
            Integer optimalIdx = 0;

            Integer earliestAvailResource = Integer.MAX_VALUE;
            Integer closestDiff = Integer.MAX_VALUE;
            Boolean foundResource = false;

            Integer i = 0;
            for (Resource holdResource : constraints.get(resource)) {
                Integer rTime = holdResource.getTimeAvailable();
                Integer diff = leastUserTime - rTime;
                if (0 <= diff && Math.abs(diff) < closestDiff) {
                    closestDiff = Math.abs(diff);
                    optimalIdx = i;
                    // Update constraint time and exit
                    foundResource = true;
                } else if (!foundResource && Math.abs(diff) < closestDiff) {
                    closestDiff = Math.abs(diff);
                    optimalIdx = i;
                }
                i++;
            }

            resourceIds.add(optimalIdx);
//            HashMap<Integer, Integer> resourceIdMapping = new HashMap<Integer, Integer>();
//            resourceIdMapping.put(s.getHoldingID(), optimalIdx);
//            resourceMap.put(resource, resourceIdMapping);

            Resource holdResource = constraints.get(resource).get(optimalIdx);
            earliestTimes.add(holdResource.getTimeAvailable());

        }

        Integer taskStart = Collections.max(earliestTimes);

        // Find earliest time
        List<Object> returnStuff = new ArrayList<Object>();
        returnStuff.add(taskStart);
        returnStuff.add(resourceIds);

        return returnStuff;
    }

    private void updateResourceTimes(Step s, List<String> resources, HashMap<String, List<Resource>> constraints, List<Integer> resourceIds, Integer taskStart) {
        // Update the earliest accessible time for each resource used
        for(Integer i = 0; i < resources.size(); i++) {
            Resource holdResource = constraints.get(resources.get(i))//get the resource id list for that type
                    .get(resourceIds.get(i));//get the resource of a sepecifc id

            holdResource.setTimeAvailable(taskStart + s.getStepTime());//update the time at that id to the amount of time it takes for the step to occur
        }
    }

    private Boolean appendResourceTaskToUser(User user, Step s, Integer taskStart, Integer userTime) {
        // TODO: Change second parameter to userTime for a task
        //Check it has enough time
        //add it to the user -> via link list
        // return true if everything works so you can update counters

        //c
        // Check if there's enough space to add task

        UserTask recent = user.getRecentTask();
        if (recent != null && recent.getNext() != null && (taskStart + userTime > recent.getNext().startTime)) {
            return false;
        }

        taskStart = user.getCurrentTime() > taskStart ? user.getCurrentTime() : taskStart;
        UserTask newTask = new UserTask(s, taskStart, userTime);

        // if resources are needed and found then Insert task into user
        if (user.getHead() == null) {
            user.setHead(newTask);
            user.setRecentTask(newTask);
            user.setTail(newTask);

            user.setAllottedTime(user.getAllottedTime() + userTime);
            user.setCurrentTime(taskStart + userTime);
            //TODO in the fall: can we fill in gaps created by waiting for a resource effectively

        } else if(recent.getNext() == null){
            newTask.setPrev(recent);
            recent.setNext(newTask);
            // Append to end of list
            user.setRecentTask(newTask);
            user.setTail(newTask);

            user.setAllottedTime(user.getAllottedTime() + userTime);
            user.setCurrentTime(taskStart + userTime);

        }else {
            // there's enough space to insert
            // Insert newTask between recent and its old next task
            UserTask nextNode = recent.getNext();
            nextNode.setPrev(newTask);
            newTask.setNext(nextNode);
            newTask.setPrev(recent);
            recent.setNext(newTask);

            user.setRecentTask(newTask);

            // Update counters
            user.setAllottedTime(user.getAllottedTime() + userTime);
            user.setCurrentTime(taskStart + userTime);
        }
        return true;
    }
}
