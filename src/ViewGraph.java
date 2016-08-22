import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Takes in list of strings representing date's glucose level and displays in interactive graph
 */
class ViewGraph {
    private static final float MINIMUM = 100;
    private static final float MAXIMUM = 120;


    @SuppressWarnings({ "unchecked", "rawtypes" })
    static LineChart<Date,Number> render(Data data, LocalDate from, LocalDate to) throws ParseException {
        List<String> records = data.calculateRange(from, to);
        final DateAxis xAxis = new DateAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        yAxis.setLabel("Glucose level");
        LineChart<Date,Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Data from " + from + " to " + to + ". (Hover over points to see data)");

        XYChart.Series<Date,Number> series = new XYChart.Series<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        for (String s : records) {
            series.getData().add(new XYChart.Data(dateFormat.parse(s.split("-")[0]), Integer.parseInt(s.split("-")[1])));
        }
        lineChart.getData().add(series);
        lineChart.setLegendVisible(false);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (XYChart.Series<Date, Number> s : lineChart.getData()) {
            for (XYChart.Data<Date, Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip("Date: " + df.format(d.getXValue()) + "\n" + "Glucose Level: " + d.getYValue()));
                d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));
                d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
            }
        }

        String lineColor  = "-fx-stroke: Black; ";
        String lineSize = "-fx-stroke-width: 0.5px;";
        Set<Node> lineNode = lineChart.lookupAll(".series0");
        for (final Node line : lineNode) {
            line.setStyle(lineColor + lineSize);
        }
        styleSeries(lineChart);
        return lineChart;
    }

    private static void styleSeries(final LineChart<Date, Number> lineChart) {
        lineChart.applyCss();

        for (XYChart.Series<Date, Number> s : lineChart.getData()) {
            for (XYChart.Data<Date, Number> d : s.getData()) {
                StringBuilder style = new StringBuilder();
                if (d.getYValue().floatValue() < MINIMUM) {
                    style.append("-fx-stroke: blue; -fx-background-color: blue, blue; ");
                } else if (d.getYValue().floatValue() < MAXIMUM) {
                    style.append("-fx-stroke: green; -fx-background-color: green, green; ");
                } else {
                    style.append("-fx-stroke: red; -fx-background-color: red, red; ");
                }

                d.getNode().setStyle(style.toString());
            }
        }
    }
}
