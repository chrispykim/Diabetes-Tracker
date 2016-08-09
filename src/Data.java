import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores date/glucose level data and provides API for storing and retrieving from text file
 */
public class Data {
    private static final String DATA_FILE = "dateFile.txt";
    private static final String DEFAULT_DIRECTORY = System.getProperty("user.dir");
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private static Path dataFilePath = Paths.get(DEFAULT_DIRECTORY, DATA_FILE);
    private boolean replace = false;
    public void setReplace(boolean replace) {
        this.replace = replace;
    }
    private ArrayList<String> data = new ArrayList<>();
    ArrayList<String> getData() {
        return data;
    }

    Data() {
        retrieveData();
    }

    void saveData(LocalDate value, String text) {
        if (!Files.exists(dataFilePath)) {
            Alert.display("Error", "Data file is nonexistent. Executable was saved in wrong directory");
            throw new IllegalStateException();
        }
        if (text.equals("") || !text.matches("^-?\\d+$")) {
            Alert.display("Error", "Glucose level was not entered correctly. Please enter a valid value before saving");
            throw new IllegalStateException();
        }
        if (alreadyContainsDate(value.format(formatter))) {
            Alert.displayWithSave("Error", "Already entered data for this date", this);
            if (replace) {
                replaceData(value, text, data);
            }
            else {
                data.add(String.format("%s-%s", value.format(formatter), text));
            }
            replace = false;
        }

        try {
            Files.delete(dataFilePath);
            Files.write(dataFilePath, data);
        } catch (IOException e) {
            Alert.display("Error", "Could not save data");
            return;
        }

        Alert.display("Success", "Saved data");
    }

    private void replaceData(LocalDate value, String text, ArrayList<String> data) {
        if (!Files.exists(dataFilePath)) {
            Alert.display("Error", "Data file is missing");
            return;
        }

        String newDate = value.format(formatter);
        for (int i = 0; i < data.size(); i++) {
            String oldDate = data.get(i).split("-")[0];
            if (oldDate.equals(newDate)) {
                data.set(i, String.format("%s-%s", newDate, text));
                break;
            }
        }
    }

    private boolean alreadyContainsDate(String date) {
        retrieveData();

        for (String s : data) {
            String currDate = s.split("-")[0];
            if (currDate.equals(date)) {
                return true;
            }
        }
        return false;
    }

    void retrieveData() {
        data.clear();

        try {
            List<String> temp = Files.readAllLines(dataFilePath);
            data.addAll(temp);
        } catch (IOException e) {
            Alert.display("Error", "Data file is nonexistent or corrupted.");
            data.clear();
            System.exit(1);
        }
    }

    String calculateMinMax() {
        StringBuilder stringBuilder = new StringBuilder();

        data.sort((o1, o2) -> {
            if (LocalDate.parse(o1.split("-")[0],formatter).isBefore(LocalDate.parse(o2.split("-")[0],formatter)))
                return -1;
            if (LocalDate.parse(o1.split("-")[0],formatter).isAfter(LocalDate.parse(o2.split("-")[0],formatter)))
                return 1;
            return 0;
        });

        String from = (data.size() > 31) ? data.get(data.size()-31) : data.get(0);
        stringBuilder.append(from).append("&").append(data.get(data.size()-1));

        return stringBuilder.toString();
    }
}
