package org.optimization;

public class Resource {
    private String name;

    private Integer id;//should match what is in the array order

    //private Integer timeAvailable;//TODO: Switch to queue THIS IS NEEDED DUE TO GAPS

    // Linked list to show all the gaps
    private ResourceGap firstGap;

    public Resource(String name, Integer id) {
        this.name = name;
        this.id = id;
        this.firstGap = new ResourceGap();
    }

    public Resource(String name, Integer id, ResourceGap firstGap) {
        this.name = name;
        this.id = id;
        this.firstGap = firstGap;
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



    public Integer canFillGap(Integer startFill, Integer fillLength) {
        ResourceGap gapToSplit = firstGap;
        //TODO: Double check that this never gets null and errors
        while(firstGap.endTime <= startFill+fillLength || firstGap.startTime > startFill){
            gapToSplit = gapToSplit.getNextGap();
        }
        if(gapToSplit.getStartTime() <= startFill && gapToSplit.getEndTime() >= (startFill+fillLength)){
            return startFill;
        }
        return gapToSplit.getStartTime();
    }

    public void fillGapAt(Integer startFill, Integer fillLength) {
        //Assuming this fits at the start time passed in
        ResourceGap prevGap = null;
        ResourceGap gapToSplit = firstGap;
        //TODO: Double check that this never gets null and errors
        while(firstGap.endTime <= startFill+fillLength || firstGap.startTime > startFill){
            prevGap = gapToSplit;
            gapToSplit = gapToSplit.getNextGap();
        }
        if(gapToSplit.getStartTime() <= startFill && gapToSplit.getEndTime() >= (startFill+fillLength)){
            if(gapToSplit.getStartTime() == startFill && gapToSplit.getEndTime() == startFill+fillLength){
                if(prevGap != null){
                    prevGap.setNextGap(gapToSplit.getNextGap());
                }
                gapToSplit.setNextGap(null);
                //TODO: will java auto delete the old gapToSplit
                //remove gap completely
            }else if(gapToSplit.getStartTime() == startFill){
                gapToSplit.setStartTime(startFill+fillLength);//is a plus 1 needed
            }else if(gapToSplit.getEndTime() == startFill+fillLength){
                gapToSplit.setEndTime(startFill-1);
            }else{
                ResourceGap newGap = new ResourceGap(gapToSplit.getNextGap(), startFill+fillLength, gapToSplit.getEndTime());
                gapToSplit.setEndTime(startFill-1);
                gapToSplit.setNextGap(newGap);
            }
            System.out.println("This should result in the below line being reached");
        }
        System.out.println("This should only be reached if the above one should");
    }
}
