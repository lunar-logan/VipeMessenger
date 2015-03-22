package com.vipe;

import java.io.File;

/**
 * Created by Anurag Gautam using IntelliJ IDEA 14 on 22-03-2015.
 * Download this awesome IDE from http://www.jetbrains.com/idea
 */
public class Config {

    public static boolean BUSY = false;

    public static final String FILE_PATH = System.getProperty("user.dir") + File.separator + "address.txt";

    public static final int PORT = 8888;

    public static final int SERVER_PORT = 8080;

    public static final int INPUT_BUFFER_SIZE = 2 * 1024 * 1024; // 2 MiB

    public static final int OUTPUT_BUFFER_SIZE = 2 * 1024 * 1024; // 2 MiB


    public static void main(String[] args) {
//        System.out.println(FILE_PATH);
    }

}
