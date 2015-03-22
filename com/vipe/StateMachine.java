package com.vipe;

import com.vipe.listener.StateMachineListener;

/**
 * Created by Anurag Gautam using IntelliJ IDEA 14 on 22-03-2015.
 * Download this awesome IDE from http://www.jetbrains.com/idea
 */
public class StateMachine {

    public static boolean busy = false;

    public static StateMachineListener onBusy = null;

    public static StateMachineListener onFree = null;

    public static void setBusy() {
        busy = true;
        if (onBusy != null) onBusy.changed();
    }

    public static void setFree() {
        busy = false;
        if (onFree != null) onFree.changed();
    }

    public static boolean isBusy() {
        return busy;
    }

}
