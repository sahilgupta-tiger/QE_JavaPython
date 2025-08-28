package allurereportGeneration;

import com.google.gson.*;
import genericwrappers.GenericWrapper;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Properties;
import java.util.stream.Collectors;

public class allureReportGeneration {

    private static final String allurePropertiesFile = "src/test/resources/allure.properties";
    private static String resultsDirectory;
    private static String reportDirectory;
    private static Process allureServeProcess;
    private static String allureExecutable = "utils/allure-2.24.1/bin/allure";
    private static String suitesFilePath;
    private static final Object lock = new Object();


    public static void loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(allurePropertiesFile)) {
            properties.load(input);
            resultsDirectory = properties.getProperty("allure.results.directory");
            reportDirectory = properties.getProperty("allure.report.directory");
            suitesFilePath = reportDirectory + "/data/suites.json";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setAllureTestcaseName(Object testcaseName) {
            // Customize scenario name
            String outlineName = (String) testcaseName; // You can adjust this string as needed

            // Update the Allure report with the new scenario name
            Allure.getLifecycle().updateTestCase(testResult ->
                    testResult.setName(outlineName));

    }

    public static void startAllureServe(String resultsDirectory) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder("powershell.exe", "allure serve " + resultsDirectory);
        builder.inheritIO();
        allureServeProcess = builder.start();
        GenericWrapper.timeOut(10000);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down Allure Serve process...");
            if (allureServeProcess != null) {
                allureServeProcess.destroy();
                System.out.println("Allure Serve process stopped.");
            }
        }));
    }


    public static void reportGeneration() {
        synchronized (lock) {
            try {
                System.out.println("::Generate an Allure Report folder with latest Results::");
                executeCommand("powershell.exe", allureExecutable + " generate " + resultsDirectory + " --clean -o " + reportDirectory);
                System.out.println("::Remove old History and Widgets files from Results folder::");
                deleteFiles(resultsDirectory + "/history");
                deleteFiles(resultsDirectory + "/widgets");
                GenericWrapper.timeOut(2000);
                System.out.println("::Copy the newly generated History and Widgets files to Results folder::");
                copyFiles(reportDirectory + "/history", resultsDirectory + "/history");
                copyFiles(reportDirectory + "/widgets", resultsDirectory + "/widgets");
                System.out.println("::Copy environment properties file::");
                copyFile("src/test/resources/environment.properties", resultsDirectory + "/environment.properties");
                GenericWrapper.timeOut(2000);
                System.out.println("::Start the Allure Server to view Reports::");
                shareableAllureReport();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void shareableAllureReport() throws IOException, InterruptedException {
        executeCommand("powershell.exe",allureExecutable+ " generate --single-file ./target/allure-results --clean --output ./target/allure");
        System.out.println("::Generated a Shareable Allure Report On FrameWork Level::");
    }

    private static void writeJsonFile(JsonObject suitesData, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(suitesData, writer);
            System.out.println("Successfully wrote JSON data to " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to write JSON data to " + filePath);
            e.printStackTrace();
        }
    }

    public static void AllureReportFilter() {
        JsonObject suitesData = readJsonFile(suitesFilePath);
        if (suitesData != null) {
            filterSuites(suitesData);
            writeJsonFile(suitesData, suitesFilePath);
            System.out.println("Filtered suites.json successfully.");
        } else {
            System.err.println("Failed to read suites.json file.");
        }
    }

    private static JsonObject readJsonFile(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JsonParser jsonParser = new JsonParser();
            return jsonParser.parse(reader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveExecutionStartTime() {
        try {
            Properties properties = new Properties();
            try (FileInputStream in = new FileInputStream(allurePropertiesFile)) {
                properties.load(in);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            String currentTime = LocalDateTime.now().format(formatter);
            properties.setProperty("execution.start.time", currentTime);

            try (OutputStream output = new FileOutputStream(allurePropertiesFile)) {
                properties.store(output, null);
            }

            String content = Files.lines(Paths.get(allurePropertiesFile))
                    .filter(line -> !line.startsWith("#"))
                    .collect(Collectors.joining("\n"));

            try (OutputStream output = new FileOutputStream(allurePropertiesFile)) {
                output.write(content.getBytes());
            }

            System.out.println("Execution start time saved to allure.properties: " + currentTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readStartTimeFromProperties() {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(allurePropertiesFile)) {
            properties.load(in);
            return properties.getProperty("execution.start.time");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void filterSuites(JsonObject suitesData) {
        JsonArray suites = suitesData.getAsJsonArray("children");
        String startTimeStr = readStartTimeFromProperties();
        if (startTimeStr == null) {
            System.err.println("Start time not found in allure.properties");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        long startTimeMillis = startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        long currentTimeMillis = Instant.now().toEpochMilli();
        long timeDifferenceMillis = currentTimeMillis - startTimeMillis;

        for (JsonElement suiteElement : suites) {
            JsonObject suite = suiteElement.getAsJsonObject();
            JsonArray children = suite.getAsJsonArray("children");

            JsonArray filteredChildren = new JsonArray();
            for (JsonElement childElement : children) {
                JsonObject child = childElement.getAsJsonObject();
                long testStartTime = child.getAsJsonObject("time").get("start").getAsLong();

                if (testStartTime >= startTimeMillis && testStartTime <= currentTimeMillis) {
                    filteredChildren.add(child);
                }
            }

            suite.add("children", filteredChildren);
        }
    }

    private static void deleteFiles(String directory) throws IOException {
        Path dir = Paths.get(directory);
        if (Files.exists(dir)) {
            Files.walk(dir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    private static void copyFiles(String sourceDir, String destDir) throws IOException {
        Path srcPath = Paths.get(sourceDir);
        Path destPath = Paths.get(destDir);
        if (Files.exists(srcPath)) {
            Files.createDirectories(destPath);
            Files.walk(srcPath)
                    .forEach(source -> {
                        try {
                            Files.copy(source, destPath.resolve(srcPath.relativize(source)), StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    private static void copyFile(String sourceFile, String destFile) throws IOException {
        Path sourcePath = Paths.get(sourceFile);
        Path destPath = Paths.get(destFile);
        Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void logFailure(String message, Throwable throwable) {
        Allure.getLifecycle().updateTestCase(result -> {
            result.setStatus(Status.FAILED);
            result.setStatusDetails(new StatusDetails()
                    .setMessage(message)
                    .setTrace(throwableToString(throwable))
            );
        });
    }

    private static String throwableToString(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append(throwable.toString()).append("\n");
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
        }
        return sb.toString();
    }

    private static void executeCommand(String... command) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.inheritIO();
        Process process = builder.start();
        process.waitFor();
    }

    public static void CleanAllureReport(){
        loadProperties();
        try {
            File reportDir = new File(reportDirectory);
            if (reportDir.exists()){

                FileUtils.cleanDirectory(reportDir);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            File resultDir = new File(resultsDirectory);
            if (resultDir.exists()){

                FileUtils.cleanDirectory(resultDir);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
