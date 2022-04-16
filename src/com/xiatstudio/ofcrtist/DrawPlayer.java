/**
 * Copyright XiatStudio Engineering 2022-2022. All rights reserved.
 */

package com.xiatstudio.ofcrtist;

import java.io.Serializable;
import java.util.Objects;

/**
 * DrawPlayer class, it defines basic property and functions for a connected user.
 */
public class DrawPlayer implements Serializable {
    private String playerName;

    private String playerIp;

    private int port;

    /**
     * DrawPlayer constructor, used to create a DrawPlayer from given parameters.
     *
     * @param name Player's nickname
     * @param ip Player's client IP address
     * @param port Player's running port
     */
    public DrawPlayer(String name, String ip, int port) {
        this.playerName = name;
        this.playerIp = ip;
        this.port = port;
    }

    /**
     * Allows player to change their player name
     *
     * @param newName New player name
     */
    public void changeName(String newName) {
        this.playerName = newName;
    }

    /**
     * Get player's nickname to client
     *
     * @return Player's nickname
     */
    public String getName() {
        return this.playerName;
    }

    /**
     * Get player's host ip
     *
     * @return Player's host ip
     */
    public String getIp() {
        return this.playerIp;
    }

    /**
     * Get player's running port, for development only
     *
     * @return Player's running port
     */
    public int getPort() {
        return this.port;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += "Player name: " + this.playerName + "\n";
        ret += "IP: " + this.playerIp + "\n";
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DrawPlayer)) {
            return false;
        }
        DrawPlayer compPlayer = (DrawPlayer) obj;
        return this.hashCode() == compPlayer.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.playerName + this.playerIp);
    }
}
