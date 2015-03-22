package com.vipe;

import com.vipe.listener.StateMachineListener;
import com.vipe.net.VipeClient;
import com.vipe.net.VipeDaemon;

import java.io.IOException;

import static com.vipe.VipeLogger.log;

/**
 * Created by Anurag Gautam using IntelliJ IDEA 14 on 22-03-2015. Download this
 * awesome IDE from http://www.jetbrains.com/idea
 */
public class Vipe {

    private static final Vipe instance = new Vipe();

    private VipeClient client = null;
    private VipeDaemon daemon = null;

    public static Vipe getInstance() {
        return instance;
    }

    private Vipe() {
    }

    public void call(String ip) {
        if (!StateMachine.isBusy()) {
            client = new VipeClient(ip, Config.PORT);
            new Thread(client).start();
        } else {
            log("Already in call");
        }
    }

    public void startServer() throws IOException {
        log("Trying to start the Vipe Server");
            daemon = new VipeDaemon();
            new Thread(daemon).start();

    }

    public void endActiveCall() {
        if (daemon != null) {
            daemon.endActiveCall();
        }
        if (client != null) {
            client.endActiveCall();
        }
    }

    public void setonFreeStateMachineListener(StateMachineListener l) {
        StateMachine.onFree = l;
    }

    public void setOnBusyStateMachineListener(StateMachineListener l) {
        StateMachine.onBusy = l;
    }

    public void stopServer() {
        if (daemon != null) {
            daemon.stopDaemon();
        }
    }

}
