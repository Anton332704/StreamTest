package helppocket;

import java.io.Serializable;

/**
 * Created by User on 08.03.2016.
 */
public class SportInfoItem implements Serializable {
    int sportId;
    String sportName;
    String sportModel;
    String team;


    public SportInfoItem(int sportId, String sportName, String sportModel, String team) {
        this.sportId = sportId;
        this.sportName = sportName;
        this.sportModel = sportModel;
        this.team = team;
    }

    public SportInfoItem() {
        this.sportId = 0;
        this.sportName = "0";
        this.sportModel = "0";
        this.team = "0";
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getSportModel() {
        return sportModel;
    }

    public void setSportModel(String sportModel) {
        this.sportModel = sportModel;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
