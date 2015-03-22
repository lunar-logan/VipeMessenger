package com.vipe;

import javax.sound.sampled.*;

import static com.vipe.VipeLogger.log;

/**
 * Created by Anurag Gautam using IntelliJ IDEA 14 on 22-03-2015.
 * Download this awesome IDE from http://www.jetbrains.com/idea
 */
public class AudioUtil {

    public static SourceDataLine getSourceLine() throws LineUnavailableException {
        SourceDataLine line = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, null);
        if (!AudioSystem.isLineSupported(info)) {
            log(String.format("'%s' line is not supported", info));
        } else {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open();
        }
        return line;
    }

    public static TargetDataLine getTargetLine() throws LineUnavailableException {
        TargetDataLine line = null;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, null);
        if (!AudioSystem.isLineSupported(info)) {
            log(String.format("'%s' line is not supported", info));
        } else {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open();
        }
        return line;
    }

}
