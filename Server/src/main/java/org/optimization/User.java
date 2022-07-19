package org.optimization;

public class User {
    // Linked List with all their tasks
    private UserTask head;
    private UserTask tail;

    // Only updated on resource tasks
    private UserTask recent;

    // Int counter of how much work User is currently tasked
    private Integer allottedTime;

    // Track where the current user is finished working
    private Integer currentTime;

    String name;



    public User () {
        this.head = null;
        this.tail = null;
        this.recent = null;

        this.allottedTime = 0;
        this.currentTime = 0;
    }

    public UserTask getHead() {
        return head;
    }

    public UserTask getTail() {
        return tail;
    }

    public UserTask getRecent() {
        return recent;
    }

    public String getName() {
        return name;
    }

    public Integer getCurrentTime(){
        return currentTime;
    }

    public UserTask getRecentTask(){
        return recent;
    }

    // make function check if user is free
    //
    public void setAllottedTime(Integer allottedTime){
        this.allottedTime = allottedTime;
    }

    public void setCurrentTime(Integer currentTime){
        this.currentTime = currentTime;
    }

    public void setHead(UserTask head){
        this.head = head;
    }

    public void setTail(UserTask tail){
        this.tail = tail;
    }

    public void setRecentTask(UserTask recent){
        this.recent = recent;
    }

    public Integer getAllottedTime(){
        return allottedTime;
    }


    // make function to update tail ptr to be head at the end of optimization
}
