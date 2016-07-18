import java.io.File;
import java.util.ArrayList;

/**
 * Takes in list of strings representing date's glucose level and displays in interactive graph
 */
class ViewGraph {
    private static final String DATA_FILE = "dateFile.txt";
    private static final String DEFAULT_DIRECTORY = System.getProperty("user.dir");

    static void render(Data data, String numEntries) {
        File destFile = new File(DEFAULT_DIRECTORY, DATA_FILE);

        if (!destFile.exists()) {
            Alert.display("Error", "Data file is nonexistent. Executable was saved in wrong directory");
            throw new IllegalStateException();
        }
        if (numEntries.equals("")) {
            Alert.display("Error", "Number of days was not entered. Please enter a value to continue");
            throw new IllegalStateException();
        }

        String[] maxMin = data.calculateMinMax(data.retrieveData()).split("-");
        int max = Integer.parseInt(maxMin[0]);
        int min = Integer.parseInt(maxMin[1]);
        System.out.println("max: " + max + ", min: " + min);
        printArray(data.retrieveData());
        System.out.println("num entries: " + numEntries);
    }

    private static void printArray(ArrayList<String> data) {
        data.forEach(System.out::println);
    }
}
