package me.lrxh.server.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {

    public static byte[] toByteArray(final File file) throws IOException {
        //Check if the file exists
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("File does not exist or is a directory: " + file);
        }

        //Check if the file is readable
        if (!file.canRead()) {
            throw new IllegalArgumentException("File cannot be read: " + file);
        }

        //Start converting process
        byte[] array;
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            array = new byte[(int) file.length()];
            int bytesRead = bis.read(array);
            if (bytesRead != file.length()) {
                throw new IOException("Failed to read entire file: " + file);
            }
        }

        return array;
    }
}
