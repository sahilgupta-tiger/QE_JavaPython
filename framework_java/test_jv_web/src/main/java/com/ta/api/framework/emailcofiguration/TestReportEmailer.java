/*
package com.ta.api.framework.emailcofiguration;

import com.jcraft.jsch.Session;

import java.io.File;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;

public class TestReportEmailer {

    public static void sendTestReportEmail(String reportDirectory, String recipientEmail) {
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

            // Send the email
            EmailSender.sendEmail(reportFile.getAbsolutePath(), recipientEmail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }


*/
/**/
