package org.optimization;


import org.recipe_processing.Recipe;
import org.utilities.database.graph.Connection;
import org.utilities.database.graph.Step;

import java.util.*;

import static org.utilities.database.relational.Main.relationDbFunctionGetConstraints;

public class Meal {
    // Hashmap of Kitchen Constraints (key=string, value=list<int>(each element is time the resource is next available))
    // List of Task Stacks (each element is a Task Class)
    HashMap<String, HashMap<Integer, Integer>> resourceMap = new HashMap<String, HashMap<Integer, Integer>>();

    public Meal () {

    }

    public void createMeal(List<Recipe> recipes, List<User> buddies, HashMap<String, List<Resource>> constraints) {

        /* Objects to pass in
            - List of Recipe keys to access database
            - List of User objects of those working in kitchen
                - First user is the user who initiated the meal
        */

        // First user is the user who initiated the meal
        User p_bud = buddies.get(0);
        // Get User kitchen constraints from database (Helper Method within Optimization?)
        // HashMap<String, List<Resource>> constraints = relationDbFunctionGetConstraints(p_bud);

        // TODO: Get the kitchen constraints from GenerateMeal?
        // Also, we need to update the hashmap with a few other constraints
        //  - Burners = Oven*4 to constraints

        // Anytime we have a new holding resource, add one to the constraint hashmap (i.e. skillet)
        // Have a list that tracks what additional kitchen resources are needed for this recipe

        // Identifies task from recipe with largest TimeLeft
        Integer maxTimeLeft = 0;
        // Time of current task
        Integer time = 0;

        // Current steps being evaluated
        List<Step> evalSteps = new ArrayList<Step>();

        // Initialize evalSteps list - get the last step in each recipe
        // TODO: Change evalSteps to a max heap
        for (Recipe recipe: recipes) {
            evalSteps.add(recipe.getFinalStep());
        }

        // What if we store the last node of each recipe and have a hashmap for the rest of the nodes


        PriorityQueue<User> users = new PriorityQueue<>();
        for (User u: buddies){
            users.add(u);
        }

        while (evalSteps.size() > 0) {
            // Find step to assign to user based on timeLeft

            // TODO: Maybe switch to priority queue?
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
            currStep = evalSteps.get(currStepId);
            evalSteps.remove(currStep);
            // for (int i = 0; i < evalSteps.size(); i++) {
            //     currStep = evalSteps.get(i);
            //     if (i == currStepId) {
            //         evalSteps.remove(currStep);
            //         break;
            //     }
            // }

            System.out.println("Working on " + currStep.getNodeID());

            // if (currStep.getNodeID() == 120.3 || currStep.getNodeID() == 122.1) {
            //     System.out.println("Debugging");
            // }

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
            User priorUser = this.mapResourceStepToUser(currStep, users, constraints);

            // Add all resource dependent previous tasks from the current node to the evalNodes
            for(Connection c: currStep.getResourceDependencies()) {
                evalSteps.add(c.getEndNode());
            }
        }

        // Iterate through user stacks and set the tail node to be the head
        // send user stacks to devices this involves twilio

        // tail -> prev -> prev -> ... -> head


    }

//    currNode = NodeE
//    time = 11
//    timeDependantNodes = [(NodeB, 10), (NodeC,15), (NodeD dependencies)]

    private User mapResourceStepToUser(
            Step s,
            PriorityQueue<User> buddies,
            HashMap<String, List<Resource>> constraints
    ) {
        // Check which user has the least amount of work so
        User user = buddies.poll();
        Integer leastUserTime = user.getLeastUserTime();
//        UserTask recent = buddies.peek().getRecent();
//
//
//
//        Integer leastUserTime = (recent != null) ? recent.getStartTime() + recent.getUserTime() : 0;
//        User user = buddies.peek();
//        Integer userIdx = 0;
//
//        // TODO: Change this process to a min heap?
//
//        for (User u: buddies) {
//            recent = u.getRecent();
//            int recentEndTime = (recent != null) ? recent.getStartTime() + recent.getUserTime() : 0;
//
//            if (recentEndTime < leastUserTime) {
//                leastUserTime = recentEndTime;
//                user = u;
//                userIdx = 0;//TODO: this should be set to an index
//            }
//        }

        // Check if constraint is available at this time
        // holding holdingResource and resourcesRequired
        //TODO: make sure this adds time depending on a user's skill level
        List<String> resources = s.getResourcesRequired();
        String holdingResource = s.getHoldingResource() != null ? s.getHoldingResource() : "";

        List<Object> taskResources = findTimeToGetConstraints(leastUserTime, s, holdingResource, resources, constraints);

        if (s.getHoldingResource() != null) {resources.add(0, holdingResource); }

        if(appendResourceTaskToUser(user, s, (Integer) taskResources.get(0), s.getUserTime())) {
            updateResourceTimes(s, resources, constraints, (List<Integer>) taskResources.get(1), (Integer) taskResources.get(0));
        } else {
            // TODO: Do something if false?
        }
        
        // Return user so that we may be able to prioritize this user to work on time dependent tasks
        return user;
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
        //TODO: make sure this checks kitchen constraints
        // The resources that we are using (by id of objects)
        List<Integer> resourceIds = new ArrayList<Integer>();

        // earliestTimes is the time of the resources
        List<Integer> earliestTimes = new ArrayList<Integer>();
        Integer holdingResource = -1;

        // Holding Resource first
        if(resourceMap.get(hResource) != null) {
            // TODO: FIX CONSTRAINTS Mapping
            // Get the id of the resource we need 
            // i.e. Bowl2: hResource=Bowl, s.getHoldingID()=2, holdingResource = 2
            //TODO: convert ResourceMap to be HashMap<String, HashMap<String, Integer>> {"bowl":{"2.2":1}} (2.2 is recipe2 bowl2)
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
            // Resource not in ResourceMap so add a new one
            // TODO: Change to <String, Integer>
            HashMap<Integer, Integer> resourceIdMapping = new HashMap<Integer, Integer>();
            resourceIdMapping.put(s.getHoldingID(), optimalIdx);
            resourceMap.put(hResource, resourceIdMapping);

            Resource holdResource = constraints.get(hResource).get(optimalIdx);
            earliestTimes.add(holdResource.getTimeAvailable());
        }


        for(String resource: tools) {
            // TODO: Add burner resource check if pot or pan
            // TODO: Add burner to resourcemap
            // Check list of elements for a resource and see if any are less than leastUserTime
            // If not, store index of resource with lowest time
            // TODO: Convert this to a function
            Integer optimalIdx = 0;

            Integer earliestAvailResource = Integer.MAX_VALUE;
            Integer closestDiff = Integer.MAX_VALUE;
            Boolean foundResource = false;

            Integer i = 0;
            // TODO: Add option to create resource constraint that may not be in hashmap yet

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

            // 
            // [1, 0, 4, 5]
            // [30, 25, 10, 15]
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
        // TODO: Add list functionality to account for gaps
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
            // TODO: Fix current time
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
