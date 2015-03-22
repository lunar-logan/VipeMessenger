package com.vipe.net;

import com.vipe.StateMachine;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.Socket;

import static com.vipe.VipeLogger.log;

/**
 * Created by Anurag Gautam using IntelliJ IDEA 14 on 22-03-2015.
 * Download this awesome IDE from http://www.jetbrains.com/idea
 */
public class VipeClient implements Runnable {
    private static Socket socket = null;
    private final String ip;
    private final int port;
    private volatile Session session = null;

    public VipeClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    private void call() {
        if (StateMachine.isBusy()) {
            log("Already in call with " + socket);
            return;
        }
        StateMachine.setBusy();
        try {
            socket = new Socket(ip, port);
            session = new Session(socket);
            Thread callThread = new Thread(session);
            callThread.start();
            callThread.join();
        } catch (IOException e) {
            log(e.getMessage());
        } catch (LineUnavailableException e) {
            log(e.getMessage());
        } catch (InterruptedException e) {
            log(e.getMessage());
        }
        StateMachine.setFree();
    }

    @Override
    public void run() {
        call();
    }

    public void endActiveCall() {
        if (session != null) {
            session.stop();
        }
    }
}
