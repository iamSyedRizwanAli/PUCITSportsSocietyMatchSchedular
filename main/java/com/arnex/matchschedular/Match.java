package com.arnex.matchschedular;

/**
 * Created by Rizwan on 05-Mar-17.
 */
public class Match {

    String teamA, teamB, time;

    public Match(String teamA, String teamB, String time) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.time = time;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return teamA + " VS " + teamB + ";" + time;
    }
}
