/**
 * Copyright XiatStudio Engineering 2022-2022. All rights reserved.
 */
package com.xiatstudio.ofcrtist;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * DrawSession class, containing basic information of a game session.
 */
public class DrawSession implements Serializable {
    /**
     * Session is waiting to start a new round
     */
    public static final int STATE_PENDING = 0;

    /**
     * Session is running
     */
    public static final int STATE_RUNNING = 1;

    /**
     * Session has just finished a game
     */
    public static final int STATE_FINISHED = 2;

    /**
     * Success
     */
    public static final int RET_OK = 0;

    /**
     * Player is already in a session
     */
    public static final int RET_ALREADY_IN = 1;

    /**
     * Session is full
     */
    public static final int RET_FULL = 2;

    private String sessionName;

    private String uid;

    private int roomId;

    private int maxRound;

    private int maxPlayer;

    private int runningState;

    private List<DrawPlayer> playerList;

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
        this.uid = UUID.randomUUID().toString();
        this.runningState = STATE_PENDING;
        this.playerList = new ArrayList<>(this.maxPlayer);
        this.roomId = Objects.hash(this.uid);
    }

    /**
     * DrawSession constructor, used to find a session via roomId
     *
     * @param roomId Requested roomId
     */
    public DrawSession(int roomId) {
        this.roomId = roomId;
        this.maxRound = -1;
        this.maxPlayer = -1;
    }

    /**
     * Process player's join request
     *
     * @param newPlayer Incoming player
     * @return Join status
     */
    public int onPlayerJoin(DrawPlayer newPlayer) {
        if (newPlayer == null) {
            return -1;
        }
        if (this.playerList.contains(newPlayer)) {
            System.out.println("Player " + newPlayer.getName() + " is already in session " + this.roomId);
            return RET_ALREADY_IN;
        }

        if (this.playerList.size() == this.maxPlayer) {
            return RET_FULL;
        }

        this.playerList.add(newPlayer);
        return RET_OK;
    }

    /**
     * Process when player quits this session
     *
     * @param quitPlayer Player who is quitting
     */
    public void onPlayerQuit(DrawPlayer quitPlayer) {
        if (quitPlayer == null) {
            return;
        }
        if (this.playerList.contains(quitPlayer)) {
            this.playerList.remove(quitPlayer);
        }
    }

    /**
     * Check if this session is a join request
     *
     * @return True if maxPlayer and maxRound are set to -1, otherwise false
     */
    public boolean isRequestJoin() {
        return this.maxPlayer == -1 && this.maxRound == -1;
    }

    /**
     * Get current session's player list
     *
     * @return List of joined players
     */
    public List<String> getPlayerStringList() {
        List<String> retList = new ArrayList<>();
        for (DrawPlayer player : this.playerList) {
            retList.add(player.getName());
        }
        return retList;
    }

    /**
     *
     */
    public List<DrawPlayer> getPlayerList() {
        return this.playerList;
    }

    /**
     * Get current session name
     *
     * @return Current session name
     */
    public String getSessionName() {
        return this.sessionName;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += "Session name: " + this.sessionName + "\n";
        ret += "Max round: " + this.maxRound + "\n";
        ret += "Max players: " + this.maxPlayer + "\n";
        ret += "UID: " + this.uid + "\n";
        ret += "hashcode: " + this.hashCode() + "\n";
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DrawSession)) {
            return false;
        }
        DrawSession compSession = (DrawSession) obj;
        return this.uid.equals(compSession.uid);
    }

    @Override
    public int hashCode() {
        return this.roomId;
    }
}
