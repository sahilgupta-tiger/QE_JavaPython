/*
package com.ta.api.framework.emailcofiguration;

import com.amazonaws.services.s3.internal.eventstreaming.Message;
import com.jcraft.jsch.Session;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class EmailSender {

    public static void sendEmail(String reportDirectory, String recipientEmail) {

        // SMTP server configuration
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Sender credentials
        final String username = "your_email_address@gmail.com";
        final String password = "your_email_password";

        // Create a new session with SMTP authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Get the most recent report file in the specified directory
            File[] files = new File(reportDirectory).listFiles();
            File reportFile = Arrays.stream(files)
                    .filter(f -> f.isFile() && f.getName().endsWith(".html"))
                    .max(Comparator.comparingLong(File::lastModified))
                    .orElse(null);

            if (reportFile == null) {
                System.out.println("No report file found in directory: " + reportDirectory);
                return;
            }

            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Test Automation Report");

            // Create a multipart message to include an attachment
            Multipart multipart = new MimeMultipart();

            // Add the report file as an attachment
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.attachFile(reportFile);
            multipart.addBodyPart(attachment);

            // Set the email message content to the multipart message
            message.setContent(multipart);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

*/
