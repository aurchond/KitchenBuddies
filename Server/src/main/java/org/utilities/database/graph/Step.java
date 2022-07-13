package org.utilities.database.graph;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NodeEntity(label="Step")
//this defines our step node and all its attributes
public class Step {
    private String name;

    @Id
    private Double nodeID;

    private Long recipeID; //number identifying recipe in database
    private Integer stepID; //step number from original recipe
    private Boolean prepStep; //defines if it is a prep step
    private String holdingResource; //what the ingredients are being kept in
    private Integer holdingID; //0 = for vegetables, 1 = for meat, 2 = mixed
    private Integer stepTime; //how much time it takes to complete step
    private Integer timeLeft; //how much time remaining to complete original recipe
    private List<String> ingredientList; //names of ingredients
    private List<Number> ingredientQuantity; //quantities of ingredients
    private List<String> resourcesRequired; //kitchen equipment like knife, cheese grater, etc.


    @Relationship(type = Connection.TYPE, direction = Relationship.OUTGOING)
    private Set<Connection> connections = new HashSet<>();

    //most used constructor
    public Step(Long recipeID, Integer stepID, Boolean prepStep, String holdingResource,
                Integer holdingID, Integer stepTime, Integer timeLeft, List<String> ingredientList,
                List<Number> ingredientQuantity, List<String> resourcesRequired) {
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
    }

    //for steps like: "preheat oven to 350 degrees", there are no ingredients or resources required
    public Step(Long recipeID, Integer stepID, Boolean prepStep, String holdingResource,
                Integer holdingID, Integer stepTime, Integer timeLeft) {
        this.recipeID = recipeID;
        this.stepID = stepID;
        this.prepStep = prepStep;
        this.holdingResource = holdingResource;
        this.holdingID = holdingID;
        this.stepTime = stepTime;
        this.timeLeft = timeLeft;
        Double step = Double.valueOf(stepID);
        this.nodeID = recipeID.doubleValue() + Double.valueOf(step/10);
        this.name = this.nodeID.toString();
    }

    public void addConnection(Step target, int distance) {
        this.connections.add(new Connection(this, target, distance));
    }

    //getter functions
    public Double getNodeID() { return this.nodeID; }
    public Long getRecipeID() { return this.recipeID; }
    public Integer getStepID() {
        return this.stepID;
    }
    public Boolean checkIfPrep() { return this.prepStep; }
    public String getHoldingResource() { return this.holdingResource; }
    public Integer getHoldingID() { return this.holdingID; }
    public Integer getStepTime() { return this.stepTime; }
    public Integer getTimeLeft() { return this.timeLeft; }
    public List<String> getIngredientList() { return this.ingredientList; }
    public List<Number> getIngredientQuantity() { return this.ingredientQuantity; }
    public List<String> getResourcesRequired() { return resourcesRequired; }

    //setter functions
    public void setRecipeID(Long ID) {
        this.recipeID = ID;

        this.nodeID = ID.doubleValue() + this.stepID; //TODO: include check to make all node IDs unique?
        this.name = this.nodeID.toString();
    }
    public void setStepID(Integer ID) {
        this.stepID = ID;

        Double step = Double.valueOf(stepID);
        this.nodeID = this.recipeID.doubleValue() + Double.valueOf(step/10);
        this.name = this.nodeID.toString();
    }
    public void setPrepStep(Boolean prep) { this.prepStep = prep; }
    public void setHoldingResource(String holdingResource) { this.holdingResource = holdingResource; }
    public void setHoldingID(Integer holdingID) { this.holdingID = holdingID; }
    public void setStepTime(Integer stepTime) { this.stepTime = stepTime; }
    public void setTimeLeft(Integer timeLeft) { this.timeLeft = timeLeft; }
    public void setIngredientList(List<String> ingredientList) { this.ingredientList = ingredientList; }
    public void setIngredientQuantity(List<Number> ingredientQuantity) { this.ingredientQuantity = ingredientQuantity; }
    public void setResourcesRequired(List<String> resourcesRequired) { this.resourcesRequired = resourcesRequired; }

// hashCode, equals, toString, no-arg constructor ommitted..
}