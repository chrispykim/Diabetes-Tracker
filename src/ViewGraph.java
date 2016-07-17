import java.util.ArrayList;

/**
 * Takes in list of strings representing date's glucose level and displays in interactive graph
 */
class ViewGraph {
    static void render(ArrayList<String> strings) {
        printArray(strings);
    }

    private static void printArray(ArrayList<String> data) {
        data.forEach(System.out::println);
        String[] maxMin = Data.calculateMinMax(data).split("-");
        int max = Integer.parseInt(maxMin[0]);
        int min = Integer.parseInt(maxMin[1]);
        System.out.println("max: " + max + ", min: " + min);
    }
}
