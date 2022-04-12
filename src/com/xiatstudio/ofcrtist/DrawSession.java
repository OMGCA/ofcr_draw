/**
 * Copyright XiatStudio Engineering 2022-2022. All rights reserved.
 */
package com.xiatstudio.ofcrtist;

import java.io.Serializable;

/**
 * DrawSession class, containing basic information of a game session.
 */
public class DrawSession implements Serializable {
    String sessionName;

    int maxRound;

    int maxPlayer;

    /**
     * DrawSession constructor
     *
     * @param name Session name
     * @param round Total numbers of round
     * @param playerCount Maximum player count
     */
    public DrawSession(String name, int round, int playerCount) {
        if (name == null) {
            this.sessionName = " ";
        } else {
            this.sessionName = name;
        }
        this.maxRound = round;
        this.maxPlayer = playerCount;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += "Session name: " + this.sessionName + "\n";
        ret += "Max round: " + this.maxRound + "\n";
        ret += "Max players: " + this.maxPlayer + "\n";
        return ret;
    }
}
