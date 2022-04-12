/**
 * Copyright XiatStudio Engineering 2022-2022. All rights reserved.
 */

package com.xiatstudio.ofcrtist.client;

import static com.xiatstudio.ofcrtist.server.MonitorThread.MONITOR_PORT;

import com.xiatstudio.ofcrtist.DrawSession;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * DrawClient class, main core of an OFCRtist client
 */
public class DrawClient {
    /**
     * Main entry of an OFCRtist client.
     */
    public DrawClient() {
        System.out.println("Welcome to OFCRtist!");
        createSession("Test server", 2, 8);
    }

    private void createSession(String sessionName, int round, int playerCount) {
        DrawSession drawSession = new DrawSession(sessionName, round, playerCount);
        String serverIp = "127.0.0.1";
        int serverPort = 16666;
        sendSession(drawSession, serverIp, serverPort);
    }

    private void sendSession(DrawSession newSession, String serverIp, int serverPort) {
        try {
            Socket socket = new Socket(serverIp, serverPort);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(newSession);
            socket.shutdownOutput();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Failed to send session information to server " + serverIp);
        }
    }
}
