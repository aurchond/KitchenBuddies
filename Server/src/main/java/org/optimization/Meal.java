package org.optimization;

public class Meal {
    // Hashmap of Kitchen Constraints (key=string, value=list<int>(each element is time the resource is next available))
    // List of Task Stacks (each element is a Task Class)


    public Meal () {

    }

    public void createMeal() {

        /* Objects to pass in
            - List of Recipe keys to access database
            - List of User objects of those working in kitchen
                - First user is the user who initiated the meal
        */

        // Get User kitchen constraints from database (Helper Method within Optimization?)

        // Get Recipe objects from graph database (Helper Method from another component?)

        // Instantiate new meal object

        /* Initialize local variables
            - maxTimeLeft = 0 (Used to identify task from recipe with largest TimeLeft)
            - curNodeId =
            - List<Nodes> evalNodes =  (stores the nodes of each recipe we are currently analyzing)
            - curTime = 0 (represents the current time for our current task
            - List<User>
         */



        // Find end node of each recipe
        // Instead of for loop to traverse, we could query the database to get the head node
        /*  for each recipe
                for recipe.node
                    if recipe.node.next is null
                        evalNodes.add(recipe.node)
        */

        /* while length(evalNodes) != 0

            // Find node to assign to user based on timeLeft
            for i in evalNodes
                if evalNodes[i].timeLeft > maxTimeLeft
                    maxTimeLeft = evalNodes[i].timeLeft
                    curNodeId = i

            // Pop most urgent task off array
            curNode = evalNodes.pop(curNodeId)

            // List<Nodes> tDependentNodes

            // Add all resource dependent previous tasks from the current node to the evalNodes
            // Add all time dependent previous tasks from the current node to a new list
            for prev in curNode->prevTask
                if prev relation with curNode == timeDependent
                    tDependentNodes.add(prev)
                else
                    evalNodes.add(prev)


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

        // Iterate through user stacks and set the tail node to be the head
        // send user stacks to devices this involves twilio

    }
}
