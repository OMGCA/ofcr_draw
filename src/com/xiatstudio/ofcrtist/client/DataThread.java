/**
 * Copyright XiatStudio Engineering 2022-2022. All rights reserved.
 */

package com.xiatstudio.ofcrtist.client;

import com.xiatstudio.ofcrtist.DrawSession;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 * DataThread class, main thread to process data between player and server
 */
public class DataThread extends Thread{
    private static int monitorPort;
    private static DrawSession currentSession;
    public DataThread(int port, DrawSession currentSession) {
        this.monitorPort = port;
        this.monitorPort++;
        this.currentSession = currentSession;
    }

    @Override
    public void run() {
        try {
            ServerSocket receiveSocket = new ServerSocket();
            receiveSocket.bind(new InetSocketAddress(this.monitorPort));
            while (true) {
                receiveUpdate(receiveSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void receiveUpdate(ServerSocket receiveSocket) {
        try {
            InputStream inputStream = new BufferedInputStream(receiveSocket.accept().getInputStream());
            String msg = new String(inputStream.readAllBytes(), "UTF-8");
            String playerChange  = msg.split(",")[0];
            int mode = Integer.parseInt(msg.split(",")[1]);
            if (mode == 0) {
                System.out.println(playerChange + " just joined in!");
            } else {
                System.out.println(playerChange + "has left.");
            }
            inputStream.close();
        } catch (IOException e) {
            System.out.println("Failed to receive from server.");
            return;
        }
    }
}
