package com.vipe.net;

import com.vipe.AudioUtil;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static com.vipe.VipeLogger.log;
import java.io.ByteArrayOutputStream;

/**
 * Created by Anurag Gautam using IntelliJ IDEA 14 on 22-03-2015. Download this
 * awesome IDE from http://www.jetbrains.com/idea
 */
public class Session implements Runnable {

    private final SourceDataLine sourceLine;
    private final TargetDataLine targetLine;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final Socket sock;

    private volatile boolean stop = false;
    private byte[] audioBuf;
    private byte[] dataBuf;

    private volatile boolean abort = false;

    public Session(Socket client) throws IOException, LineUnavailableException {
        this.sock = client;
        this.in = new DataInputStream(client.getInputStream());
        this.out = new DataOutputStream(client.getOutputStream());

        this.sourceLine = AudioUtil.getSourceLine();
        this.targetLine = AudioUtil.getTargetLine();
    }

    public void start() {
        // Start the lines
        startLines();
        log("Audio Lines setup complete...100%");

        initBuffers();
        log("Buffer setup complete...100%");

        log("Setting up the mic capture thread");
        Thread captureThread = setupMicCaptureThread();
        log("mic capture thread complete...100%");

        log("Starting the capture thread");
        captureThread.start();
        log("Started mic capture thread");

        ByteArrayOutputStream about = new ByteArrayOutputStream();
        log("stop=" + stop + "\tabort=" + abort);
        while (!stop && !abort) {
            try {
                // Read the payload size
//                int size = in.readInt();

//                if (size < 0) {
                    // Seems the person in front wants to end the call, fine by me;)
//                    stop();
//                    break;
//                }

                int numBytes = in.read(dataBuf, 0, dataBuf.length);

                sourceLine.write(dataBuf, 0, numBytes);
            } catch (IOException e) {
                e.printStackTrace();
                log(e.getMessage());
                // Probably socket is closed
                abort = true;
                break;
            }
        }
        closeLines();

        try {
            // Wait for capture thread to complete
            captureThread.join();
        } catch (InterruptedException e) {
            log(e.getMessage());
        }
    }

    private Thread setupMicCaptureThread() {
        //Start a thread to read mic data
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stop && !abort) {
                    int read = targetLine.read(audioBuf, 0, audioBuf.length);
                    if (read > 0) {
                        try {
//                            out.writeInt(read);
                            out.write(audioBuf, 0, read);
                            out.flush();
                        } catch (IOException e) {
                            log(e.getMessage());
                            // Possibly the socket is closed
                            abort = true;
                            break;
                        }
                    }
                }

                // End the active call, by sending negative size reply
//                try {
//                    out.writeInt(-1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    log("Error ending the connection, " + e.getMessage());
//                }
            }
        });
    }

    private void initBuffers() {
        assert targetLine != null;
        this.audioBuf = new byte[targetLine.getBufferSize() / 5];
        this.dataBuf = new byte[targetLine.getBufferSize() / 5];

    }

    private void startLines() {
        sourceLine.start();
        targetLine.start();
    }

    private void closeLines() {
        sourceLine.drain();
        sourceLine.stop();
        targetLine.stop();

        sourceLine.close();
        targetLine.close();
    }

    public void stop() {
        stop = true;
        try {
            sock.shutdownInput();
        } catch (IOException e) {
            log("Error while shutting down the input. " + e.getMessage());
        }
    }

    @Override
    public void run() {
        start();
    }
}
