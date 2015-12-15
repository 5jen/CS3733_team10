package Calendar;

/**
 * Created by yx on 12/14/15.
 */
public class MyEvent {
    private String summary;
    private String location;
    private String startTime;
    private String endTime;

    /**
     * Constructor
     * @param summary
     * @param location
     * @param startTime
     * @param endTime
     */
    public MyEvent(String summary, String location, String startTime, String endTime){
        this.summary = summary;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getSummary(){
        return this.summary;
    }

    public String getLocation(){
        return this.location;
    }

    public String getStartTime(){
        return this.startTime;
    }

    public String getEndTime(){
        return this.endTime;
    }
}
