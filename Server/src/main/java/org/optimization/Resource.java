package org.optimization;

public class Resource {
    private String name;

    private Integer id;//should match what is in the array order

    private Integer timeAvailable;//TODO: Switch to queue debate? don't think it is needed


    public Resource (String name, Integer id, Integer timeAvailable) {
        this.name = name;
        this.id = id;
        this.timeAvailable = timeAvailable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTimeAvailable() {
        return timeAvailable;
    }

    public void setTimeAvailable(Integer timeAvailable) {
        this.timeAvailable = timeAvailable;
    }
}
