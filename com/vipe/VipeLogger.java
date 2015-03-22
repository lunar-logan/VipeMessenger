package com.vipe;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by Anurag Gautam using IntelliJ IDEA 14 on 22-03-2015.
 * Download this awesome IDE from http://www.jetbrains.com/idea
 */
public class VipeLogger {
    public static final Logger LOGGER = Logger.getLogger("vipe_logger");
    private static FileHandler fh;
    static {
        try {
            fh = new FileHandler("fairy.log");
            LOGGER.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);
        } catch (IOException ex) {
            Logger.getLogger(VipeLogger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(VipeLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void log(String msg) {
        LOGGER.log(Level.INFO, msg);
    }
}
