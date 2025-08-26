package helperfunctions;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The common methods which will be used across project
 *
 * @author gayathri
 */
public class CommonUtils {

    /**
     * This method is to create or Read the file
     *
     * @param parentFile            the file that needs to create or read
     * @param folderName            the folder to which the file should append
     * @param fileNameWithExtension the file extension
     * @return returns the file
     */
    public static File createOrReadFile(File parentFile, final String folderName, final String fileNameWithExtension) {
        if (parentFile == null) {
            parentFile = new File(fileNameWithExtension);
            return parentFile;
        }
        if (!folderName.isEmpty()) {
            parentFile = new File(parentFile, FilenameUtils.getName(folderName));
        }
        if (!fileNameWithExtension.isEmpty()) {
            parentFile = new File(parentFile, FilenameUtils.getName(fileNameWithExtension));
        }
        return parentFile;
    }

    /**
     * This function will Generate Random String based on required length specified by the user
     *
     * @param size the length of the string
     * @return returns the random string of required length
     */
    public static String generateRandomString(int size) {
        return RandomStringUtils.randomAlphabetic(size);
    }

    /**
     * This function will Generate Number String based on required length specified by the user
     *
     * @param size the length of the number
     * @return returns the random number of required length
     */
    public static long generateRandomNumber(int size) {
        long number = (long) Math.floor(Math.random() * 1.0E8D) + 10000000L;

        number = Long.parseLong(Long.toString(number).substring(0, size));
        return number;
    }

    /**
     * This function will Generate Random Email ID based on required length specified by the user
     *
     * @param size the length of the email Id
     * @return returns the random email id of required length. Example: abcd@gmail.com -- If required length is 4
     */
    public String emailidGenerator(int size) {
        String userName = this.generateRandomString(size);
        return userName.concat("@gmail.com");
    }

    /**
     * This function will Generate Future Dates based on requirement specified by the user
     *
     * @param num amount the amount of date or time to be added to the current date.
     * @return returns the future date
     */
    public String futureDateGenerator(int num) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(5, num);
        Date currentDatePlusOne = c.getTime();
        return dateFormat.format(currentDatePlusOne);
    }

    public static String readFile(String filePath) {

        File file = new File(filePath);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader br
                    = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null)
                stringBuilder.append(st);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }

}
