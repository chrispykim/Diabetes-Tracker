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
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    ArrayList<String> getData() {
        return data;
    }

    private ArrayList<String> data;

    Data() {
        data = new ArrayList<>();
        retrieveData();
    }

    void saveData(LocalDate value, String text) {
        File destFile = new File(DEFAULT_DIRECTORY, DATA_FILE);

        if (!destFile.exists()) {
            Alert.display("Error", "Data file is nonexistent. Executable was saved in wrong directory");
            throw new IllegalStateException();
        }
        if (text.equals("")) {
            Alert.display("Error", "Glucose level was not entered. Please enter a value before saving");
            throw new IllegalStateException();
        }

        retrieveData();
        if (alreadyContainsDate(value.format(formatter))) {
            Alert.displayWithSave("Error", "Already entered data for this date", destFile, value, text, this);
            return;
        }

        try {
            Files.write(destFile.toPath(), Arrays.asList(value.format(formatter), text), StandardOpenOption.APPEND);
        } catch (IOException e) {
            Alert.display("Error", "Could not save data");
            return;
        }

        Alert.display("Success", "Saved data");
    }

    void replaceData(File destFile, LocalDate value, String text) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).equals(value.format(formatter))) {
                data.set(i, value.format(formatter));
                data.set(i+1, text);
                break;
            }
        }

        try {
            Files.delete(destFile.toPath());
            Files.write(destFile.toPath(), data, StandardOpenOption.CREATE);
        } catch (IOException e) {
            Alert.display("Error", "Could not save data");
            return;
        }


        Alert.display("Success", "Saved data");
    }

    private boolean alreadyContainsDate(String date) {
        for (String s : data) {
            if (s.equals(date)) {
                return true;
            }
        }
        return false;
    }

    ArrayList<String> retrieveData() {
        data.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(
                new File(DEFAULT_DIRECTORY, DATA_FILE).getAbsolutePath()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            Alert.display("Error", "Data file is nonexistent or corrupted.");
            data.clear();
        }

        /*while (data.size() > SIX_MONTHS) {
            data.remove(0);
        }*/

        return data;
    }

    String calculateMinMax(ArrayList<String> data) {
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        for (String s : data) {
            if ((count % 2) != 0) {
                int curr = Integer.parseInt(s);
                max = (curr > max) ? curr : max;
                min = (curr < min) ? curr : min;
            }
            count++;
        }

        stringBuilder.append(max).append("-").append(min);

        return stringBuilder.toString();
    }
}
