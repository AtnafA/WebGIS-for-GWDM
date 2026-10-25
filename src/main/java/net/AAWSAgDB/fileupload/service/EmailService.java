package net.AAWSAgDB.fileupload.service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());
    
    private final String host = "172.20.4.10";
    private final String fromAddress = "HGSuperAdmin@ecdswc.com.et";
    private final String password = "Admin@Passc9de";
    private final String fromName = "Atnaf Abreham";

    // Your existing method remains perfectly here...
    public void sendPreAuthenticateMail(String[] recipients, String userType) throws MessagingException, UnsupportedEncodingException {
        if (recipients == null || recipients.length == 0) {
            throw new IllegalArgumentException("Recipient list cannot be empty.");
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromAddress, password);
            }
        });
        session.setDebug(true);

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(fromAddress, fromName));

        InternetAddress[] toAddresses = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            toAddresses[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, toAddresses);

        InternetAddress[] bccAddresses = { new InternetAddress("dosty2020@outlook.com") };
        msg.setRecipients(Message.RecipientType.BCC, bccAddresses);

        msg.setSubject("Hi this is test Mail");

        if ("22".equals(userType)) {
            int maximum = 1000, minimum = 1;
            String plaintext = "c0de" + (minimum + (int) (Math.random() * ((maximum - minimum) + 1)));
            msg.setContent(plaintext, "text/plain");
        } else {
            msg.setContent("You are successfully Privileged !!", "text/plain");
        }

        Transport.send(msg, msg.getAllRecipients());
    }

    // ─── ADD THIS NEW METHOD HERE TO FIX THE COMPILER ERROR ───
    public void sendRegistrationConfirmation(String recipientEmail) throws MessagingException, UnsupportedEncodingException {
        if (recipientEmail == null || recipientEmail.trim().isEmpty() || recipientEmail.equalsIgnoreCase("none")) {
            return; // Skip if no email address was provided
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromAddress, password);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(fromAddress, fromName));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        
        InternetAddress[] bccAddresses = { new InternetAddress("dosty2020@outlook.com") };
        msg.setRecipients(Message.RecipientType.BCC, bccAddresses);
        
        msg.setSubject("Hi this is test Mail");
        msg.setContent("You are successfully Registered !!", "text/plain");

        Transport.send(msg);
    }
}
