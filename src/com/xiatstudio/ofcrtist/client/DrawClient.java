/**
 * Copyright XiatStudio Engineering 2022-2022. All rights reserved.
 */

package com.xiatstudio.ofcrtist.client;

import static com.xiatstudio.ofcrtist.server.MonitorThread.MONITOR_PORT;

import com.xiatstudio.ofcrtist.DrawPlayer;
import com.xiatstudio.ofcrtist.DrawSession;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 * DrawClient class, main core of an OFCRtist client
 */
public class DrawClient {
    private static DrawPlayer currentPlayer;

    private static final int PORT = 16667;

    private static final String TEST_SERVER = "127.0.0.1";

    private DrawSession currentSession;

    /**
     * Main entry of an OFCRtist client.
     */
    public DrawClient() {
        System.out.println("Welcome to OFCRtist!");
        System.out.print("Enter your nickname: ");
        Scanner nameReader = new Scanner(System.in);
        String name = nameReader.next();
        System.out.print("Do you want to create(1) or join a session(2)? ");
        int option = nameReader.nextInt();
        createPlayer(name);
        if (option == 1) {
            System.out.print("Session name: ");
            String sessionName = nameReader.next();
            System.out.print("Max round: ");
            int maxRound = nameReader.nextInt();
            System.out.print("Max player: ");
            int maxPlayer = nameReader.nextInt();
            createSession(sessionName, maxRound, maxPlayer);
        } else {
            System.out.println("Session share id: ");
            int shareId = nameReader.nextInt();
            joinSession(shareId);
        }
        return;
    }

    private void createPlayer(String name) {
        currentPlayer = new DrawPlayer(name, TEST_SERVER);
    }

    private void createSession(String sessionName, int round, int playerCount) {
        DrawSession drawSession = new DrawSession(sessionName, round, playerCount);
        sendReq(drawSession, TEST_SERVER, MONITOR_PORT);
        System.out.print("Successfully created session " + currentSession.getSessionName());
        System.out.println(", you can share this session to others via " + currentSession.hashCode());
    }

    private void joinSession(int roomId) {
        DrawSession requestSession = new DrawSession(roomId);
        sendReq(requestSession, TEST_SERVER, MONITOR_PORT);
        if (currentSession == null) {
            System.out.println("Request session does not exist.");
            return;
        }
        System.out.print("Successfully joined session " + currentSession.getSessionName());
        System.out.println(", you can share this session to others via " + currentSession.hashCode());
        List<String> playerList = currentSession.getPlayerList();
        System.out.println("Players in current session:");
        for (String playerName : playerList) {
            System.out.println(playerName);
        }
    }

    private void sendReq(DrawSession newSession, String serverIp, int serverPort) {
        try {
            Socket socket = new Socket(serverIp, serverPort);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(newSession);
            outputStream.writeObject(this.currentPlayer);
            receiveSession();
            socket.shutdownOutput();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Failed to send session information to server " + serverIp);
        }
    }

    private void receiveSession() {
        System.out.println("Waiting for server's response.");
        try {
            ServerSocket receiveSocket = new ServerSocket();
            receiveSocket.bind(new InetSocketAddress(PORT));
            Socket socket = receiveSocket.accept();
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            currentSession = (DrawSession) objectInputStream.readObject();

            socket.shutdownInput();
            objectInputStream.close();
            socket.close();
            receiveSocket.close();
        } catch (IOException e) {
            System.out.println("Failed to receive from server.");
            return;
        } catch (ClassNotFoundException e) {
            return;
        }
    }
}
