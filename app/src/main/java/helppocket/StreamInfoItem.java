package helppocket;

import android.content.Intent;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by User on 01.03.2016.
 */
public class StreamInfoItem implements Serializable {
    String name;
    Boolean finish;
    String firstOpponent;
    String secondOpponent;
    int sportId;
    int top;
    String videoId;


    public StreamInfoItem(String name, Boolean finish, String firstOpponent, String secondOpponent, int sportId, int top, String videoId) {
        this.name = name;
        this.finish = finish;
        this.firstOpponent = firstOpponent;
        this.secondOpponent = secondOpponent;
        this.sportId = sportId;
        this.top = top;
        this.videoId = videoId;
    }

    public StreamInfoItem() {
        this.name = "default";
        this.finish = true;
        this.firstOpponent = "default";
        this.secondOpponent = "default";
        this.sportId = 1;
        this.top = 0;
        this.videoId = "0";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getFirstOpponent() {
        return firstOpponent;
    }

    public void setFirstOpponent(String firstOpponent) {
        this.firstOpponent = firstOpponent;
    }

    public String getSecondOpponent() {
        return secondOpponent;
    }

    public void setSecondOpponent(String secondOpponent) {
        this.secondOpponent = secondOpponent;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }



    @Override
    public boolean equals(Object o) {
//        int vidO1 = Integer.parseInt(this.videoId);
//        int vidO2 = Integer.parseInt(((StreamInfoItem) o).videoId);
        if(this.videoId.equals((((StreamInfoItem) o).videoId)) )return true;
        else return false;
    }


}
