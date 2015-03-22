package com.vipe.net;

import com.vipe.Config;
import com.vipe.StateMachine;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.vipe.VipeLogger.log;

/**
 * Created by Anurag Gautam using IntelliJ IDEA 14 on 22-03-2015. Download this
 * awesome IDE from http://www.jetbrains.com/idea
 */
public class VipeDaemon implements Runnable {

    private final ServerSocket server;
    private volatile boolean stopDaemon;
    private volatile Session session = null;

    public VipeDaemon() throws IOException {
        server = new ServerSocket(Config.PORT);
        stopDaemon = false;
    }

    public void stopDaemon() {
        try {
            server.close();
        } catch (IOException e) {
            log(e.getMessage());
        }
        stopDaemon = true;
    }

    @Override
    public void run() {
        log("Server started listening on " + server.getInetAddress());
        while (!stopDaemon) {
            Socket client = null;
            try {
                client = server.accept();
                handle(client);
            } catch (IOException e) {
                log(e.getMessage());
            } finally {
                try {
                    if (client != null) {
                        client.close();
                    }
                } catch (IOException e) {
                    log(e.getMessage());
                }
            }
        }
    }

    private void handle(Socket client) {
        // Test if the user is already in call with somebody
        if (StateMachine.isBusy()) {
            return;
        }
        StateMachine.setBusy();
        int value = JOptionPane.showConfirmDialog(null, String.format("Call from '%s', do you want to take?", client.getInetAddress()));
        if (value == 0) {
            Thread sessionThread = null;
            try {
                session = new Session(client);
                sessionThread = new Thread(session);
                sessionThread.start();
                sessionThread.join();
            } catch (IOException e) {
                log(e.getMessage());
            } catch (LineUnavailableException e) {
                log(e.getMessage());
            } catch (InterruptedException e) {
                log(e.getMessage());
            } finally {
                StateMachine.setFree();
            }
        } else {
            StateMachine.setFree();
        }
    }

    public void endActiveCall() {
        if (session != null) {
            session.stop();
        }
    }

}
