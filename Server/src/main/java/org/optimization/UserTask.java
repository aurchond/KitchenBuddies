package org.optimization;

import org.utilities.database.graph.Step;

public class UserTask {
    UserTask next;
    UserTask prev;

    Step step;
    Integer startTime;

    Integer userTime;

    public UserTask(Step step, Integer startTime, Integer userTime) {
        this.step = step;
        this.startTime = startTime;
        this.userTime = userTime;
    }

    public void setNext(UserTask next) {
        this.next = next;
    }

    public void setPrev(UserTask prev) {
        this.prev = prev;
    }

    public UserTask getNext() {
        return next;
    }

    public UserTask getPrev() {
        return prev;
    }

    public Step getStep() {
        return step;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public Integer getUserTime() {
        return userTime;
    }
}
