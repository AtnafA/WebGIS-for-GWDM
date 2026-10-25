package net.AAWSAgDB.fileupload.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.stereotype.Service;

@Service
public class FileProcessingService {

    /**
     * Safely reads an entire incoming file stream into a binary byte array.
     * Replaces all four redundant, flawed static methods with a stable chunk buffer stream.
     * 
     * @param file The target input data stream
     * @return Fully populated byte array, or null if the stream is empty
     * @throws IOException If file network transmission fails
     */
    public byte[] convertStreamToByteArray(InputStream file) throws IOException {
        if (file == null) {
            return null;
        }

        // FIXED: Using a dynamic chunk buffer loop instead of file.available() 
        // to prevent partial, corrupted file uploads over network connections.
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            byte[] dataChunk = new byte[4096]; // Read data in safe 4KB chunks
            int bytesRead;
            
            while ((bytesRead = file.read(dataChunk, 0, dataChunk.length)) != -1) {
                buffer.write(dataChunk, 0, bytesRead);
            }
            
            buffer.flush();
            return buffer.toByteArray();
        } catch (IOException e) {
            System.err.println("Exception encountered during file stream processing compilation.");
            e.printStackTrace();
            throw e;
        }
    }
}