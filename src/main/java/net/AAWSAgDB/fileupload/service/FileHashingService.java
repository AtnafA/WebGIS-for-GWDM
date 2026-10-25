package net.AAWSAgDB.fileupload.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Service;

@Service
public class FileHashingService {

    /**
     * Generates a unique SHA-256 cryptographic check checksum hash for an incoming data file stream array.
     * Used to prevent duplicate file uploads and ensure file data stream consistency checks.
     *
     * @param dataStream Binary byte array of the incoming uploaded file context
     * @return 64-character hexadecimal signature string
     */
    public String calculateSHA256(byte[] dataStream) throws NoSuchAlgorithmException {
        if (dataStream == null || dataStream.length == 0) {
            return "";
        }

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(dataStream); 
        
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            // Convert byte data to positive integer hexadecimal characters
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0'); // Enforce leading double digit padding rules
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}