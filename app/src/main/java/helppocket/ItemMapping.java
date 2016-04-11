package helppocket;

/**
 * Created by User on 08.03.2016.
 */
public class ItemMapping {
    String sportType;
    String streamName;

    public ItemMapping(String sportType, String streamName) {
        this.sportType = sportType;
        this.streamName = streamName;
    }

    public ItemMapping() {
        this.sportType = "0";
        this.streamName = "0";
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }
}
