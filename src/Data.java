import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Stores date/glucose level data and provides API for storing and retrieving from text file
 */
class Data {
    private static final String DATA_FILE = "dateFile.txt";
    private static final String DEFAULT_DIRECTORY = System.getProperty("user.dir");
    private static final int SIX_MONTHS = 360;

    static void saveData(LocalDate value, String text) {
        File destFile = new File(DEFAULT_DIRECTORY, DATA_FILE);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        if (!destFile.exists()) {
            Alert.display("Error", "Data file is nonexistent. Executable was saved in wrong directory");
            throw new IllegalStateException();
        }
        if (text.equals("")) {
            Alert.display("Error", "Glucose level was not entered. Please enter a value before saving");
            throw new IllegalStateException();
        }
        if (alreadyContainsDate(value.format(formatter))) {
            Alert.display("Error", "Already entered data for this date");
            throw new IllegalStateException();
        }

        try {
            Files.write(destFile.toPath(), Arrays.asList(value.format(formatter), text), StandardOpenOption.APPEND);
        } catch (IOException e) {
            Alert.display("Error", "Could not save data");
        }

        Alert.display("Success", "Saved data");
    }

    private static boolean alreadyContainsDate(String date) {
        for (String s : retrieveData()) {
            if (s.equals(date)) {
                return true;
            }
        }
        return false;
    }

    static ArrayList<String> retrieveData() {
        ArrayList<String> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            Alert.display("Error", "Data file is nonexistent or corrupted.");
            data.clear();
        }

        while (data.size() > SIX_MONTHS) {
            data.remove(0);
        }

        return data;
    }
}
