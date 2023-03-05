package org.optimization;


import org.recipe_processing.Recipe;
import org.utilities.database.graph.Connection;
import org.utilities.database.graph.Step;

import java.util.*;

public class Meal {
    // Hashmap of Kitchen Constraints
    // key = Resource name (i.e. "Bowl")
    // value = Hashmap<String, Int> (i.e. Bowl2.3, 1)
    // Bowl2.3 represents the 2nd bowl in RecipeId 3. The value 1 is a map to bowl 1 in our constraints Hashmap
    // mapRecipeResToCon = mapRecipeResourceToConstraint
    HashMap<String, HashMap<String, Integer>> mapRecipeResToCon = new HashMap<String, HashMap<String, Integer>>();
    HashMap<String, String> burnerMap = new HashMap<String, String>();
    HashMap<String, List<Resource>> constraints = new HashMap<String, List<Resource>>();

    public Meal () {
        burnerMap.put("skillet", "burner");
        burnerMap.put("pot", "burner");
        burnerMap.put("pan", "burner");
    }

    public void createMeal(List<Recipe> recipes, List<User> buddies, HashMap<String, List<Resource>> kConstraints) {
        /* Objects to pass in
            - List of Recipe keys to access database
            - List of User objects of those working in kitchen
                - First user is the user who initiated the meal
        */

        constraints = kConstraints;
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
        PriorityQueue<Step> evalSteps = new PriorityQueue<>();
        //List<Step> evalSteps = new ArrayList<Step>();

        // Initialize evalSteps list - get the last step in each recipe
        // TODO: Change evalSteps to a max heap
        for (Recipe recipe : recipes) {
            evalSteps.add(recipe.getFinalStep());
        }

        PriorityQueue<User> users = new PriorityQueue<>();
        for (User u : buddies) {
            users.add(u);
        }

        while (evalSteps.size() > 0) {
            // Find step to assign to user based on timeLeft
            Step currStep = evalSteps.poll();
            System.out.println("Working on " + currStep.getNodeID());

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
            User priorUser = this.mapResourceStepToUser(currStep, users);

            // Add all resource dependent previous tasks from the current node to the evalNodes
            for (Connection c : currStep.getResourceDependencies()) {
                evalSteps.add(c.getEndNode());
            }
        }
    }

    private User mapResourceStepToUser(
            Step s,
            PriorityQueue<User> buddies
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

        if (holdingResource.equals("")) {
            System.out.println("Holding Resource is NULL?!");
        }

        List<Object> taskResources = findTimeToGetConstraints(leastUserTime, s, holdingResource, resources);

        if (s.getHoldingResource() != null) {
            resources.add(0, holdingResource);
        }

        if (appendResourceTaskToUser(user, s, (Integer) taskResources.get(0), s.getUserTime())) {
            updateResourceTimes(s, resources, constraints, (List<Integer>) taskResources.get(1), (Integer) taskResources.get(0));
        } else {
            // TODO: Do something if false?
            // Go back to top of function
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
            List<String> tools) {
        //TODO: make sure this checks kitchen constraints
        // The resources that we are using (by id of objects)
        List<Integer> resourceIds = new ArrayList<Integer>();

        // earliestTimes is the time of the resources
        List<Integer> earliestTimes = new ArrayList<Integer>();

        // Split holdingResource "Bowl2.3" into "Bowl" and "2.3"
        String sHR = hResource;
        String hResourceType = sHR.substring(0, sHR.length() - 1);
        // resource used in mapRecipeResToCon
        Integer resourceId = Character.getNumericValue(sHR.charAt(sHR.length() - 1));
        String recResId = Integer.toString(resourceId) + Long.toString(s.getRecipeID());

        // Holding Resource first
        getResourceTimes(hResourceType, recResId, leastUserTime, resourceIds, earliestTimes, false);

        if (burnerMap.get(hResourceType) != null) {
            getResourceTimes("burner", hResource, leastUserTime, resourceIds, earliestTimes, true);
        }

        // TODO: Add this back when we add constraints based on recipe
//         for (String resource : tools) {
//             // TODO: Add burner resource check if pot or pan
//             // TODO: Add burner to mapRecipeResToCon
//             // Check list of elements for a resource and see if any are less than leastUserTime
//             // If not, store index of resource with lowest time
//             // TODO: Convert this to a function
//             Integer optimalIdx = 0;

//             Integer earliestAvailResource = Integer.MAX_VALUE;
//             Integer closestDiff = Integer.MAX_VALUE;
//             Boolean foundResource = false;

//             Integer i = 0;
//             // TODO: Add option to create resource constraint that may not be in hashmap yet

//             for (Resource holdResource : constraints.get(resource)) {
//                 Integer rTime = holdResource.getTimeAvailable();
//                 Integer diff = leastUserTime - rTime;
//                 if (0 <= diff && Math.abs(diff) < closestDiff) {
//                     closestDiff = Math.abs(diff);
//                     optimalIdx = i;
//                     // Update constraint time and exit
//                     foundResource = true;
//                 } else if (!foundResource && Math.abs(diff) < closestDiff) {
//                     closestDiff = Math.abs(diff);
//                     optimalIdx = i;
//                 }
//                 i++;
//             }

//             // 
//             // [1, 0, 4, 5]
//             // [30, 25, 10, 15]
//             resourceIds.add(optimalIdx);
// //            HashMap<Integer, Integer> resourceIdMapping = new HashMap<Integer, Integer>();
// //            resourceIdMapping.put(s.getHoldingID(), optimalIdx);
// //            mapRecipeResToCon.put(resource, resourceIdMapping);

//             Resource holdResource = constraints.get(resource).get(optimalIdx);
//             earliestTimes.add(holdResource.getTimeAvailable());

//         }

        Integer taskStart = Collections.max(earliestTimes);

        // Find earliest time
        List<Object> returnStuff = new ArrayList<Object>();
        returnStuff.add(taskStart);
        returnStuff.add(resourceIds);

        return returnStuff;
    }

    private void getResourceTimes(String hResourceType, String recResId, Integer leastUserTime, List<Integer> resourceIds, List<Integer> earliestTimes, Boolean isBurner) {
        if (mapRecipeResToCon.get(hResourceType) != null) {
            // mapRecipeResToCon = {"bowl":{"2.3":1}}
            // hResourceType = bowl, resource = "2.3" (2=resourceId, 3=RecipeId), constraintMapId = 1
            // 
            // constraints = {"bowl":[Resource1, Resource2, Resource3]}
            // constraintMapId maps to a bowl in constraints that the chef will use
            // 
            Integer constraintMapId = mapRecipeResToCon.get(hResourceType).get(recResId);
            Resource holdResource = constraints.get(hResourceType).get(constraintMapId);
            earliestTimes.add(holdResource.getTimeAvailable());
            resourceIds.add(constraintMapId);
        } else {
            // Resource needs to be added to mapRecipeResToCon
            Integer optimalIdx = 0;

            Integer earliestAvailResource = Integer.MAX_VALUE;
            Integer closestDiff = Integer.MAX_VALUE;
            Boolean foundResource = false;

            Integer i = 0;
            for (Resource holdResource : constraints.get(hResourceType)) {
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
            // Resource not in mapRecipeResToCon so add a new one
            HashMap<String, Integer> resourceIdMapping = new HashMap<String, Integer>();
            resourceIdMapping.put(recResId, optimalIdx);
            mapRecipeResToCon.put(hResourceType, resourceIdMapping);

            Resource holdResource = constraints.get(hResourceType).get(optimalIdx);
            earliestTimes.add(holdResource.getTimeAvailable());
        }
    }

    private void updateResourceTimes(Step s, List<String> resources, HashMap<String, List<Resource>> constraints, List<Integer> resourceIds, Integer taskStart) {
        // Update the earliest accessible time for each resource used
        // TODO: Add list functionality to account for gaps
        for (Integer i = 0; i < resources.size(); i++) {
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

        } else if (recent.getNext() == null) {
            newTask.setPrev(recent);
            recent.setNext(newTask);
            // Append to end of list
            user.setRecentTask(newTask);
            user.setTail(newTask);

            user.setAllottedTime(user.getAllottedTime() + userTime);
            // TODO: Fix current time
            user.setCurrentTime(taskStart + userTime);

        } else {
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
