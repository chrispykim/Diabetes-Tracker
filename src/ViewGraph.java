import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Takes in list of strings representing date's glucose level and displays in interactive graph
 */
class ViewGraph {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    static LineChart<Date,Number> render(Data data, LocalDate from, LocalDate to) throws ParseException {
        List<String> records = data.calculateRange(from, to);
        final DateAxis xAxis = new DateAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        yAxis.setLabel("Glucose level");
        LineChart<Date,Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Data from " + from + " to " + to);

        XYChart.Series<Date,Number> series = new XYChart.Series<>();
        series.setName("Hover mouse over points to see value");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        for (String s : records) {
            series.getData().add(new XYChart.Data(dateFormat.parse(s.split("-")[0]), Integer.parseInt(s.split("-")[1])));
        }
        lineChart.getData().add(series);

        for (XYChart.Series<Date, Number> s : lineChart.getData()) {
            for (XYChart.Data<Date, Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        d.getXValue().toString() + "\n" +
                                "Glucose Level : " + d.getYValue()));

                //Adding class on hover
                d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));

                //Removing class on exit
                d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
            }
        }

        return lineChart;
    }
}
