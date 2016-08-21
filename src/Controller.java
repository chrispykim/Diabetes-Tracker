import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Controller class that handles user events
 */
public class Controller extends Application {
    private Stage window;
    private Scene start;
    private Scene enterData;
    private Scene viewData;
    private DatePicker datePicker, datePickerFrom, datePickerTo;
    private Data data;
    private TextField glucoseLevel;
    private Button addData;
    private Button viewGraph;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale.setDefault(Locale.KOREAN);
        window = primaryStage;
        data = new Data();
        // set up UI logic
        setUpStartScene();
        setUpEnterDataScene();
        datePickerFrom = new DatePicker();
        datePickerTo = new DatePicker();
        setUpViewDataScene();
        // initialize
        window.setScene(start);
        window.setTitle("Glucose levels tracker");
        window.show();
    }

    private void setUpViewDataScene() {
        VBox viewDataLayout = new VBox(20);

        HBox selectEntriesPane = new HBox(10);
        setUpDatePickers();
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isAfter(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        datePickerTo.setDayCellFactory(dayCellFactory);
        Label enterNumEntries1 = new Label("View data from");
        Label enterNumEntries2 = new Label("to");
        selectEntriesPane.getChildren().addAll(enterNumEntries1, datePickerFrom, enterNumEntries2, datePickerTo);
        selectEntriesPane.setAlignment(Pos.CENTER);

        Button viewDataButton = new Button("View trends");
        viewDataButton.setOnAction(e -> {
            if (datePickerFrom.getValue().isBefore(datePickerTo.getValue())) {
                data.retrieveData();
                try {
                    VBox chartViewBox = new VBox(20);
                    Button backFromViewData = new Button("Back");
                    backFromViewData.setOnAction(e1 -> {
                        window.setScene(viewData);
                        addData.requestFocus();
                    });
                    LineChart<Date,Number> chart = ViewGraph.render(data, datePickerFrom.getValue(), datePickerTo.getValue());
                    chartViewBox.getChildren().addAll(chart, backFromViewData);
                    chartViewBox.setAlignment(Pos.CENTER);
                    VBox.setVgrow(chart, Priority.ALWAYS);
                    chartViewBox.setPadding(new Insets(20, 20, 20, 0));
                    Scene viewChart = new Scene(chartViewBox, 600, 500);
                    window.setScene(viewChart);
                    window.setFullScreen(true);
                    window.setMaximized(true);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
            else {
                Alert.display("Error", "Invalid date range");
                setUpDatePickers();
            }
        });

        Button backFromViewData = new Button("Back");
        backFromViewData.setOnAction(e -> {
            window.setMaximized(false);
            window.setFullScreen(false);
            window.centerOnScreen();
            window.setScene(start);
            addData.requestFocus();
        });

        viewDataLayout.getChildren().addAll(selectEntriesPane, viewDataButton, backFromViewData);
        viewDataLayout.setAlignment(Pos.CENTER);
        viewData = new Scene(viewDataLayout, 600, 500);
    }

    private void setUpDatePickers() {
        List<String> range = data.calculateRange();
        LocalDate fromDate = LocalDate.parse(range.get(0).split("-")[0], Data.formatter);
        LocalDate toDate = LocalDate.parse(range.get(range.size() - 1).split("-")[0], Data.formatter);
        datePickerFrom.setValue(fromDate);
        datePickerTo.setValue(toDate);
        datePickerFrom.setEditable(false);
        datePickerTo.setEditable(false);
        datePickerFrom.requestFocus();
    }

    private void setUpEnterDataScene() {
        VBox enterDataLayout = new VBox(20);

        HBox addDatePane = new HBox(10);
        Label enterDateLabel = new Label("Enter date: ");
        datePicker = new DatePicker(LocalDate.now());
        addDatePane.getChildren().addAll(enterDateLabel, datePicker);
        addDatePane.setAlignment(Pos.CENTER);

        HBox addGlucosePane = new HBox(10);
        glucoseLevel = new TextField();
        Label enterGlucoseLabel = new Label("Enter the glucose level: ");
        addGlucosePane.getChildren().addAll(enterGlucoseLabel, glucoseLevel);
        addGlucosePane.setAlignment(Pos.CENTER);

        Button saveData = new Button("Save");
        saveData.setOnAction(e ->
                data.saveData(datePicker.getValue(), glucoseLevel.getText())
        );

        Button backFromEnterData = new Button("Back");
        backFromEnterData.setOnAction(e -> {
            window.setScene(start);
            viewGraph.requestFocus();
        });

        enterDataLayout.setPadding(new Insets(20, 20, 20, 20));
        enterDataLayout.getChildren().addAll(addDatePane, addGlucosePane, saveData, backFromEnterData);
        enterDataLayout.setAlignment(Pos.CENTER);
        enterData = new Scene(enterDataLayout, 600, 500);
    }

    private void setUpStartScene() {
        VBox startLayout = new VBox(20);

        Label mainMenuLabel = new Label("Main Menu");
        mainMenuLabel.setFont(Font.font(mainMenuLabel.getFont().getName(), 30));

        addData = new Button("Enter Data");
        addData.setOnAction(e -> {
            window.setScene(enterData);
            datePicker.setEditable(false);
            datePicker.setValue(LocalDate.now());
            glucoseLevel.clear();
            glucoseLevel.requestFocus();
        });

        viewGraph = new Button("View data");
        viewGraph.setOnAction(e -> {
            window.setScene(viewData);
            data.retrieveData();
            setUpDatePickers();
        });

        startLayout.setAlignment(Pos.CENTER);
        startLayout.getChildren().addAll(mainMenuLabel, addData, viewGraph);
        start = new Scene(startLayout, 600, 500);
    }
}
