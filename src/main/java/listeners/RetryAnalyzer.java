package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int min=1;
    private static final String retryconfigpath="src/main/java/listeners/RetryConfig.properties";

    @Override
    public boolean retry(ITestResult iTestResult) {

        Properties props = new Properties();
        try (
                FileInputStream fis = new FileInputStream(retryconfigpath)) {
            props.load(fis);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final int max= Integer.parseInt(props.getProperty("maxCount"));
        if(min<max) {
            System.out.println("Retrying test case: "+iTestResult.getName()+" Attempt: "+min+" out of "+max);
            min++;
            return true;
        }
        return false;
    }
}
