import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Locale;

/**
 * Controller class that handles user events
 */
public class Controller extends Application {
    private Stage window;
    private Scene start, enterData, viewData;
    private DatePicker datePicker;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale.setDefault(Locale.KOREAN);
        window = primaryStage;
        // set up UI logic
        setUpStartScene();
        setUpEnterDataScene();
        setUpViewDataScene();
        // initialize
        window.setScene(start);
        window.setTitle("Glucose levels tracker");
        window.show();
    }

    private void setUpViewDataScene() {
        VBox viewDataLayout = new VBox();

        Button backFromViewData = new Button("Back");
        backFromViewData.setOnAction(e -> window.setScene(start));

        viewDataLayout.getChildren().addAll(backFromViewData);
        viewDataLayout.setAlignment(Pos.CENTER);
        viewData = new Scene(viewDataLayout, 600, 500);
    }

    private void setUpEnterDataScene() {
        VBox enterDataLayout = new VBox(20);

        HBox addDatePane = new HBox(10);
        Label enterDateLabel = new Label("Enter date: ");
        datePicker = new DatePicker(LocalDate.now());
        addDatePane.getChildren().addAll(enterDateLabel, datePicker);
        addDatePane.setAlignment(Pos.CENTER);

        HBox addGlucosePane = new HBox(10);
        TextField glucoseLevel = new TextField();
        Label enterGlucoseLabel = new Label("Enter the glucose level: ");
        addGlucosePane.getChildren().addAll(enterGlucoseLabel, glucoseLevel);
        addGlucosePane.setAlignment(Pos.CENTER);

        Button saveData = new Button("Save");
        saveData.setOnAction(e -> Data.saveData(datePicker.getValue(), glucoseLevel.getText()));

        Button backFromEnterData = new Button("Back");
        backFromEnterData.setOnAction(e -> window.setScene(start));

        enterDataLayout.setPadding(new Insets(20, 20, 20, 20));
        enterDataLayout.getChildren().addAll(addDatePane, addGlucosePane, saveData, backFromEnterData);
        enterDataLayout.setAlignment(Pos.CENTER);
        enterData = new Scene(enterDataLayout, 600, 500);
    }

    private void setUpStartScene() {
        VBox startLayout = new VBox(20);

        Label mainMenuLabel = new Label("Main Menu");
        mainMenuLabel.setFont(Font.font(mainMenuLabel.getFont().getName(), 30));

        Button addData = new Button("Enter Data");
        addData.setOnAction(e -> {
            window.setScene(enterData);
            datePicker.setEditable(false);
            datePicker.setValue(LocalDate.now());
        });

        Button viewGraph = new Button("View data");
        viewGraph.setOnAction(e -> {
            window.setScene(viewData);
            ViewGraph.render(Data.retrieveData());
        });

        startLayout.setAlignment(Pos.CENTER);
        startLayout.getChildren().addAll(mainMenuLabel, addData, viewGraph);
        start = new Scene(startLayout, 600, 500);
    }
}
