package org.optimization;

public class User implements Comparable<User> {
    String email;
    Integer skillLevel;
    // Linked List with all their tasks
    private UserTask head;
    private UserTask tail;
    // Only updated on resource tasks
    private UserTask recent;
    // Int counter of how much work User is currently tasked
    private Integer allottedTime;
    // Track where the current user is finished working
    private Integer currentTime;

    public User(String email) {
        this.head = null;
        this.tail = null;
        this.recent = null;

        this.allottedTime = 0;
        this.currentTime = 0;
        this.email = email;
        this.skillLevel = 2;//TODO: Check what default should be
    }

    public User(String email, Integer skillLevel) {
        this.head = null;
        this.tail = null;
        this.recent = null;

        this.allottedTime = 0;
        this.currentTime = 0;
        this.email = email;
        this.skillLevel = skillLevel;
    }

    public Integer getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(Integer skillLevel) {
        this.skillLevel = skillLevel;
    }

    public UserTask getHead() {
        return head;
    }

    public void setHead(UserTask head) {
        this.head = head;
    }

    public UserTask getTail() {
        return tail;
    }

    public void setTail(UserTask tail) {
        this.tail = tail;
    }

    public UserTask getRecent() {
        return recent;
    }

    public String getEmail() {
        return email;
    }

    public Integer getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Integer currentTime) {
        this.currentTime = currentTime;
    }

    public UserTask getRecentTask() {
        return recent;
    }

    public void setRecentTask(UserTask recent) {
        this.recent = recent;
    }

    public Integer getAllottedTime() {
        return allottedTime;
    }

    // make function check if user is free
    //
    public void setAllottedTime(Integer allottedTime) {
        this.allottedTime = allottedTime;
    }

    public void setName() {
        this.email = email;
    }
    // make function to update tail ptr to be head at the end of optimization

    public void printStepList() {
        UserTask traverse = this.tail;
        String stepList = this.email + ": ";
        String detailedList = "";

        System.out.println(this.email + " Steps");

        while (traverse != null) {
            stepList += traverse.step.getNodeID();
            detailedList = "NodeID: " + traverse.step.getNodeID() + " Start Time: "
                    + traverse.startTime + " Step Time: " + traverse.step.getStepTime()
                    + " User Time: " + traverse.getUserTime();

            System.out.println(detailedList);
            traverse = traverse.getPrev();
            if (traverse != null) {
                stepList += " -> ";
            }
        }

        stepList += " total allotted time: " + this.allottedTime.toString();

        System.out.println(stepList);
    }
    public Integer getLeastUserTime(){
        return (recent != null) ? recent.getStartTime() + recent.getUserTime() : 0;
    }

    @Override
    public int compareTo(User o) {
        return this.getLeastUserTime().compareTo(o.getLeastUserTime() );
    }
}
