import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Takes in list of strings representing date's glucose level and displays in interactive graph
 */
class ViewGraph {
    static void render(Data data, LocalDate from, LocalDate to) {
//        if (numEntries.equals("")) {
//            Alert.display("Error", "Number of days was not entered. Please enter a value to continue");
//            throw new IllegalStateException();
//        }

//        String[] maxMin = data.calculateMinMax(data.getData()).split("-");
//        int max = Integer.parseInt(maxMin[0]);
//        int min = Integer.parseInt(maxMin[1]);
//        System.out.println("max: " + max + ", min: " + min);
//        printArray(data.getData());
//        System.out.println("num entries: " + numEntries);

        System.out.println("From " + from + " to " + to);
    }

    public static void printArray(ArrayList<String> data) {
        data.forEach(System.out::println);
    }
}
