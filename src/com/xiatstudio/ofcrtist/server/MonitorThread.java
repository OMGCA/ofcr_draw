/**
 * Copyright XiatStudio Engineering 2022-2022. All rights reserved.
 */

package com.xiatstudio.ofcrtist.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * MonitorThread class, this thread is used to monitor new request to create a session.
 */
public class MonitorThread extends Thread {
    private static ServerSocket serverSocket;

    private static Socket socket;

    private static final int PORT = 16666;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(PORT));
            System.out.println("Waiting for new session creation request.");

            socket = serverSocket.accept();
        } catch (IOException e) {
            System.out.println("Failed to create ServerSocket for MonitorThread.");
        }
    }
}
