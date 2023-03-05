package org.optimization;

public class ResourceGap {
    ResourceGap nextGap;
    //ResourceGap prev;

    Integer startTime;
    Integer endTime;

    public ResourceGap() {
        this.nextGap = null;
        this.startTime = 0;
        this.endTime = Integer.MAX_VALUE;
    }

    public ResourceGap(Integer startTime) {
        this.nextGap = null;
        this.startTime = startTime;
        this.endTime = Integer.MAX_VALUE;
    }

    public ResourceGap(ResourceGap nextGap, Integer startTime, Integer endTime) {
        this.nextGap = nextGap;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ResourceGap getNextGap() {
        return nextGap;
    }

    public void setNextGap(ResourceGap nextGap) {
        this.nextGap = nextGap;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }
}
