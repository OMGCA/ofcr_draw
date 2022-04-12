/**
 * Copyright XiatStudio Engineering 2022-2022. All rights reserved.
 */

package com.xiatstudio.ofcrtist.server;

/**
 * DrawServer class, main core of a OFCRtist server
 */
public class DrawServer {

    /**
     * DrawServer constructor, it will start various of necessary threads.
     */
    public DrawServer() {
        MonitorThread monitorThread = new MonitorThread();
        monitorThread.start();
    }
}
