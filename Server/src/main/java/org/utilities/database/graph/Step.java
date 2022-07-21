package org.utilities.database.graph;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.*;

@NodeEntity(label="Step")
//this defines our step node and all its attributes
public class Step {
    /*
    This class represents a Step in a Recipe and contains the following

    Double - id - RecipeId.StepId
    Long - recipeId - identifies which recipe it is a part of (TODO: we will have to store in a db which integers match with which recipe titles)
    Integer - StepId - the step number from the original recipe
    List of Sting - ingredients - list of ingredients needed in this step
    List of float - quantity - amount and unit for each ingredient in list above
    Boolean - isPrepStep - True if this step involved preparation instead of cooking
    List of Strings - toolsRequired - list of additional told to help with the step (eg knife)
    String - holdingResource - the resource that contains the ingredients in this step (eg. cutting board, bowl, pan etc)
    Integer - holdingId - keeps track of the various holding resources (ie, bowl1 vs bowl2)
    Float - stepTime - the time it takes to complete the step in minutes TODO: need to check if we have access to a time class
    Float - recipeTimeLeft - the time it takes to complete all the steps before this one in minutes
    String - instructions - The text instructions for this step
    TODO: does the step need to know there is a time dependency
    # TODO: double check this(how do we keep the same element between steps) - For appliance such as stove we will keep a count of which elements are free and therefore it is part of toolsRequired?
     */
    private String name;

    @Id
    private Double nodeID;

    private Long recipeID; //number identifying recipe in database
    private Integer stepID; //step number from original recipe
    private Boolean prepStep; //defines if it is a prep step
    private String holdingResource; //what the ingredients are being kept in
    private Integer holdingID; //0 = for vegetables, 1 = for meat, 2 = mixed
    private Integer stepTime; //how much time it takes to complete step
    private Integer userTime; // how much of the task time is required for the user to be actively working
    private Integer timeLeft; //how much time remaining to complete original recipe

    private List<String> ingredientList; //names of ingredients (i.e. cup of sugar)

    private List<Float> ingredientQuantity; //quantities of ingredients [[8, cups]

//    private List<Map.Entry<Float, String>> ingredientQuantity; //quantities of ingredients [[8, cups]
    private List<String> resourcesRequired; //kitchen equipment like knife, cheese grater, etc.

    private String instructions;

    private String action; // what is the step actually doing? i.e. frying, baking, chopping, dicing, etc.


    @Relationship(type = Connection.TYPE, direction = Relationship.OUTGOING)
    private Set<Connection> connections = new HashSet<>();

    //most used constructor
    public Step(Long recipeID, Integer stepID, Boolean prepStep, String holdingResource,
                Integer holdingID, Integer stepTime, Integer timeLeft, List<String> ingredientList,
                List<Float> ingredientQuantity, List<String> resourcesRequired, Integer userTime, String action) {
        this.recipeID = recipeID;
        this.stepID = stepID;
        this.prepStep = prepStep;
        this.holdingResource = holdingResource;
        this.holdingID = holdingID;
        this.stepTime = stepTime;
        this.timeLeft = timeLeft;
        this.ingredientList = ingredientList;
        this.ingredientQuantity = ingredientQuantity;
        this.resourcesRequired = resourcesRequired;
        Double step = Double.valueOf(stepID);
        this.nodeID = recipeID.doubleValue() + Double.valueOf(step/10);
        this.name = this.nodeID.toString();
        this.userTime = userTime;
        this.action = action;
    }

    //for steps like: "preheat oven to 350 degrees", there are no ingredients or resources required
    public Step(Long recipeID, Integer stepID, Boolean prepStep, String holdingResource,
                Integer holdingID, Integer stepTime, Integer timeLeft, Integer userTime) {
        this.recipeID = recipeID;
        this.stepID = stepID;
        this.prepStep = prepStep;
        this.resourcesRequired = new ArrayList<String>();
        this.holdingResource = holdingResource;
        this.holdingID = holdingID;
        this.stepTime = stepTime;
        this.timeLeft = timeLeft;
        Double step = Double.valueOf(stepID);
        this.nodeID = recipeID.doubleValue() + Double.valueOf(step/10);
        this.name = this.nodeID.toString();
        this.userTime = userTime;
    }

    public Step() {

    }

    public void addConnection(Step target, int distance) {
        this.connections.add(new Connection(this, target, distance));
    }

    //getter functions
    public Long getRecipeID() { return this.recipeID; }
    public Integer getStepID() {
        return this.stepID;
    }
    public Double getNodeID() {
        return this.nodeID;
    }
    public Boolean checkIfPrep() { return this.prepStep; }
    public String getHoldingResource() { return this.holdingResource; }
    public Integer getHoldingID() { return this.holdingID; }
    public Integer getStepTime() { return this.stepTime; }
    public Integer getTimeLeft() { return this.timeLeft; }
    public List<String> getIngredientList() { return this.ingredientList; }
    public List<Float> getIngredientQuantity() { return this.ingredientQuantity; }
    public List<String> getResourcesRequired() { return resourcesRequired; }
    public Set<Connection> getConnections() { return connections; }
    public List<Connection> getTimeDependencies() {
        List<Connection> stepConnections = new ArrayList<Connection>();

        for (Connection c: connections) {
            if (c.getConnectionTime() >= 0) {
                stepConnections.add(c);
            }
        }

        return stepConnections;
    }
    public List<Connection> getResourceDependencies() {
        //TODO: find all Resource dependencies
        // return connections;
        List<Connection> stepConnections = new ArrayList<Connection>();

        for (Connection c: connections) {
            if (c.getConnectionTime() < 0) {
                stepConnections.add(c);
            }
        }

        return stepConnections;
    }

    //setter functions
    public void setRecipeID(Long ID) { this.recipeID = ID; }
    public void setStepID(Integer ID) { this.stepID = ID; }
    public void setNodeID(Double ID) { this.nodeID = ID; }
    public void setPrepStep(Boolean prep) { this.prepStep = prep; }
    public void setHoldingResource(String holdingResource) { this.holdingResource = holdingResource; }
    public void setHoldingID(Integer holdingID) { this.holdingID = holdingID; }
    public void setStepTime(Integer stepTime) { this.stepTime = stepTime; }
    public void setTimeLeft(Integer timeLeft) { this.timeLeft = timeLeft; }
    public void setIngredientList(List<String> ingredientList) { this.ingredientList = ingredientList; }
    public void setIngredientQuantity(List<Float> ingredientQuantity) { this.ingredientQuantity = ingredientQuantity; }
    public void setResourcesRequired(List<String> resourcesRequired) { this.resourcesRequired = resourcesRequired; }
    public void addConnection(Step end) {
        Connection c = new Connection(this, end);
        this.connections.add(c);
    }

    public boolean hasResourceConnection(Step step) {
        for (Connection c: this.connections) {
            if(c.getEndNode() == step) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMultipleConnection() {
        if(this.connections.size()>1) {
                return true;
        }
        return false;
    }

    public void deleteConnection(Step endNode) {
        //TODO: Fill in
    }

    public Integer getUserTime() {
        return userTime;
    }

    public void setUserTime(Integer userTime) {
        this.userTime = userTime;
    }

    public void setInstructions(String instructions) { this.instructions = instructions;}

// hashCode, equals, toString, no-arg constructor ommitted..
}