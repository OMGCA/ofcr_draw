/**
 * Copyright XiatStudio Engineering 2022-2022. All rights reserved.
 */

package com.xiatstudio.ofcrtist.server;

import com.xiatstudio.ofcrtist.DrawSession;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * MonitorThread class, this thread is used to monitor new request to create a session.
 */
public class MonitorThread extends Thread {
    /**
     * Port to monitor new session request
     */
    public static final int MONITOR_PORT = 16666;

    private static ServerSocket serverSocket;

    private static Socket socket;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(MONITOR_PORT));
            System.out.println("Waiting for new session creation request.");

            socket = serverSocket.accept();
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            DrawSession newSession = (DrawSession) objectInputStream.readObject();
            objectInputStream.close();

            System.out.println(newSession.toString());
            socket.close();
        } catch (IOException e) {
            System.out.println("Failed to create ServerSocket for MonitorThread.");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to parse DrawSession class.");
        }
    }
}
