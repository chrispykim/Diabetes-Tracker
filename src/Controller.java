import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
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
    private static final String DATA_FILE = "dateFile.txt";
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

        HBox addDataPane = new HBox(10);
        Label enterDateLabel = new Label("Enter date: ");
        datePicker = new DatePicker(LocalDate.now());
        addDataPane.getChildren().addAll(enterDateLabel, datePicker);
        addDataPane.setAlignment(Pos.CENTER);

        Button backFromEnterData = new Button("Back");
        backFromEnterData.setOnAction(e -> window.setScene(start));
        Button saveData = new Button("Save");
        saveData.setOnAction(e -> saveData(datePicker.getValue()));

        enterDataLayout.getChildren().addAll(addDataPane, saveData, backFromEnterData);
        enterDataLayout.setAlignment(Pos.CENTER);
        enterData = new Scene(enterDataLayout, 600, 500);
    }

    private void setUpStartScene() {
        VBox startLayout = new VBox(20);

        Label mainMenuLabel = new Label("Main Menu");
        mainMenuLabel.setFont(Font.font("Cambria", 30));

        Button addData = new Button("Enter Data");
        addData.setOnAction(e -> {
            window.setScene(enterData);
            datePicker.setEditable(false);
        });
        Button viewGraph = new Button("View data");
        viewGraph.setOnAction(e -> window.setScene(viewData));

        startLayout.setAlignment(Pos.CENTER);
        startLayout.getChildren().addAll(mainMenuLabel, addData, viewGraph);
        start = new Scene(startLayout, 600, 500);
    }

    private void saveData(LocalDate value) {
        System.out.println(value);
    }
}
