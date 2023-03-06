package org.optimization;

import java.util.ArrayList;
import java.util.List;

public class User implements Comparable<User> {
    String email;
    Integer skillLevel;
    // Linked List with all their tasks
    private UserTask head;
    private UserTask tail;
    // Only updated on resource tasks
    private List<UserTask> recents;
    // Int counter of how much work User is currently tasked
    private Integer allottedTime;
    // Track where the current user is finished working
    private List<Integer> currentTime;

    public User(String email) {
        this.head = null;
        this.tail = null;
        this.recents = new ArrayList<>();

        this.allottedTime = 0;
        this.currentTime = new ArrayList<>();
        this.email = email;
        this.skillLevel = 2;//TODO: Check what default should be
    }

    public User(String email, Integer skillLevel) {
        this.head = null;
        this.tail = null;
        this.recents = new ArrayList<>();

        this.allottedTime = 0;
        this.currentTime = new ArrayList<>();
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

    public List<UserTask> getRecents() {
        return recents;
    }
    public UserTask getRecentsIdx(Integer idx) {
        if(idx == -1){
            return null;
        }
        return this.recents.get(idx);
    }

    public String getEmail() {
        return email;
    }

    public List<Integer> getCurrentTime() {
        return this.currentTime;
    }

    public Integer getCurrentIdx(Integer idx) {
        if(idx == -1){
            return 0;
        }
        return this.currentTime.get(idx);
    }

    public void setCurrentTime(List<Integer> currentTime) {
        this.currentTime = currentTime;
    }

    public void setCurrentIdx(Integer currentTime, Integer idx) {
        if(idx == -1){
            this.currentTime.add(currentTime);
        }else{
            this.currentTime.set(idx, currentTime);
        }
        //CURRENTLY ONLY ADDING TIMES NOT REMOVING ANY WHEN A GAP GETS TOO SMALL
    }

    public void setRecents(List<UserTask> recents) {
        this.recents = recents;
    }
    public void setRecentTask(UserTask recents, Integer idx) {
        if(idx == -1){
            this.recents.add(recents);
        }else{
            this.recents.set(idx, recents);
        }
        //CURRENTLY ONLY ADDING TIMES NOT REMOVING ANY WHEN A GAP GETS TOO SMALL
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
    public Integer getLeastUserTime(Integer userTime){
        if(recents.size() == 0){
            return -1;
        }else{
            for(int i=0; i<recents.size(); i++){
                if(recents.get(i).getNext() == null){
                    return i;
                }else if(recents.get(i).getNext().getStartTime() - currentTime.get(i) > userTime){
                    return i;
                }
            }
            return recents.size()-1;
        }
    }

    @Override
    public int compareTo(User o) {
        return this.getAllottedTime().compareTo(o.getAllottedTime());
    }
}
