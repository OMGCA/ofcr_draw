/**
 * Copyright XiatStudio Engineering 2022-2022. All rights reserved.
 */
package com.xiatstudio.ofcrtist.server;

import com.xiatstudio.ofcrtist.DrawPlayer;
import com.xiatstudio.ofcrtist.DrawSession;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * SessionInfoThread class, main thread to handle session specific data to players
 */
public class SessionInfoThread extends Thread {
    private static final int CONNECT_TIMEOUT = 5000;

    @Override
    public void run() {
    }

    public static void onSessionUpdate(DrawSession drawSession, String playerChange, int mode) {
        if (drawSession == null || playerChange == null) {
            return;
        }

        for (DrawPlayer player : drawSession.getPlayerList()) {
            if (player.getName().equals(playerChange)) {
                continue;
            }
            String clientIp = player.getIp();
            int clientPort = player.getPort();
            clientPort++;
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(clientIp, clientPort), CONNECT_TIMEOUT);
                String msg = playerChange + "," + mode;
                OutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
                outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("Failed to connect to " + player.getName() + " on " + clientIp + ":" + clientPort);
                continue;
            }
        }
    }
}
