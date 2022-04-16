/**
 * Copyright XiatStudio Engineering 2022-2022. All rights reserved.
 */

package com.xiatstudio.ofcrtist.server;

import com.xiatstudio.ofcrtist.DrawPlayer;
import com.xiatstudio.ofcrtist.DrawSession;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * DrawSessionManager class, it manages all session related operations in one server.
 */
public class DrawSessionManager {
    private static final Object LOCK = new Object();

    private static DrawSessionManager sInstance;

    private static ConcurrentMap<Integer, DrawSession> drawSessionMap = new ConcurrentHashMap<>();

    private DrawSessionManager() {
    }

    /**
     * DrawSessionManager singleton
     *
     * @return DrawSessionManager singleton instance
     */
    public static DrawSessionManager getInstance() {
        synchronized (LOCK) {
            if (sInstance == null) {
                synchronized (LOCK) {
                    sInstance = new DrawSessionManager();
                }
                return sInstance;
            }
            return sInstance;
        }
    }

    /**
     * Handles client's request to create or return a requested session
     *
     * @param incomingSession New session info
     * @param requestedPlayer Request source
     * @return Updated session information
     */
    public DrawSession handleRequest(DrawSession incomingSession, DrawPlayer requestedPlayer) {
        if (incomingSession == null) {
            return null;
        }
        if (incomingSession.isRequestJoin()) {
            return joinSession(incomingSession.hashCode(), requestedPlayer);
        }
        return createSession(incomingSession, requestedPlayer);
    }

    private DrawSession createSession(DrawSession incomingSession, DrawPlayer requestedPlayer) {
        if (drawSessionMap.containsKey(incomingSession.hashCode())) {
            return null;
        }
        this.drawSessionMap.put(incomingSession.hashCode(), incomingSession);
        incomingSession.onPlayerJoin(requestedPlayer);
        System.out.println("Created session, room id " + incomingSession.hashCode() + ", request by player " + requestedPlayer.getName());
        return incomingSession;
    }

    private DrawSession joinSession(int requestId, DrawPlayer requestedPlayer) {
        System.out.println("Player " + requestedPlayer.getName() + " request to join session " + requestId);
        if (!drawSessionMap.containsKey(requestId)) {
            System.out.println("Session id " + requestId + " does not exist.");
            return null;
        }
        DrawSession currentSession = drawSessionMap.get(requestId);
        currentSession.onPlayerJoin(requestedPlayer);
        return currentSession;
    }
}
