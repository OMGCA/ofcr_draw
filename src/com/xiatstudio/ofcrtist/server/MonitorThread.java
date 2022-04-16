/**
 * Copyright XiatStudio Engineering 2022-2022. All rights reserved.
 */

package com.xiatstudio.ofcrtist.server;

import com.xiatstudio.ofcrtist.DrawPlayer;
import com.xiatstudio.ofcrtist.DrawSession;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    /**
     * Client's listening port
     */
    public static final int CLIENT_PORT = 16667;

    private static final int SERVER_SLEEP = 500;

    private static final int CONNECT_TIMEOUT = 5000;

    private static ServerSocket serverSocket;

    private static Socket socket;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(MONITOR_PORT));
            while (true) {
                System.out.println("Waiting for new session creation request.");
                socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                DrawSession newSession = (DrawSession) objectInputStream.readObject();
                DrawPlayer requestedPlayer = (DrawPlayer) objectInputStream.readObject();
                DrawSession currentSession = DrawServer.drawSessionMgr.handleRequest(newSession, requestedPlayer);
                Thread.sleep(SERVER_SLEEP);
                sendSessionToClient(socket.getInetAddress().toString().replace("/", ""),
                    requestedPlayer.getPort(), currentSession);
                Thread.sleep(SERVER_SLEEP);
                SessionInfoThread.onSessionUpdate(currentSession, requestedPlayer.getName(), 0);
                objectInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to create ServerSocket for MonitorThread.");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to parse DrawSession class.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendSessionToClient(String clientIp, int clientPort, DrawSession currentSession) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(clientIp, clientPort), CONNECT_TIMEOUT);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(currentSession);
            socket.shutdownOutput();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Failed to connect to client.");
            return;
        }
    }
}
