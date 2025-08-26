package apicoreutils;
import java.io.File;
import java.io.IOException;

public class DirectoryCleaner {

    // Hardcoded directory path
    private static final String TARGET_DIRECTORY_PATH = "target";

    // Method to delete contents of the hardcoded directory
    public static void deleteAllureResultsContents() throws IOException {
        File directory = new File(TARGET_DIRECTORY_PATH);
        deleteDirectoryContents(directory);
    }

    // Method to delete contents of a directory without deleting the directory itself
    // Method to delete contents of a directory without deleting the directory itself
    private static void deleteDirectoryContents(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();  // List all files and subdirectories
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);  // Recursively delete directories
                } else {
                    if (!file.delete()) {
                        throw new IOException("Failed to delete file: " + file);
                    }
                }
            }
        }
    }

    // Helper method to delete a directory and its contents recursively
    private static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();  // List contents of the directory
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);  // Correctly calling the deleteDirectory method
                } else {
                    if (!file.delete()) {
                        throw new IOException("Failed to delete file: " + file);
                    }
                }
            }
        }

        if (!directory.delete()) {
            throw new IOException("Failed to delete directory: " + directory);
        }
    }


}
