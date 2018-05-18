package com.arnex.matchschedular;

/**
 * Created by Rizwan on 05-Mar-17.
 */
public class Team {

    String teamName, teamShift;

    public Team(String teamName, String teamShift) {
        this.teamName = teamName;
        this.teamShift = teamShift;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamShift() {
        return teamShift;
    }

    public void setTeamShift(String teamShift) {
        this.teamShift = teamShift;
    }
}
