/**
 * Copyright XiatStudio Engineering 2022-2022. All rights reserved.
 */

package com.xiatstudio.ofcrtist;

/**
 * DrawPlayer class, it defines basic property and functions for a connected user.
 */
public class DrawPlayer {
    String playerName;

    String playerUid;

    String playerIp;

    /**
     * DrawPlayer constructor, used to create a DrawPlayer from given parameters.
     *
     * @param name Player's nickname
     * @param ip Player's client IP address
     */
    public DrawPlayer(String name, String ip) {
        this.playerName = name;
        this.playerIp = ip;
    }
}
